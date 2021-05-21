package blue.kafka.internal.core.offset;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Kafka Consumer record offset 内存管理
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-04
 */
public class MemoryOffsetManager implements OffsetManager {
	private static Logger logger = LoggerFactory.getLogger(MemoryOffsetManager.class);

	private static final int MAX_DEEP = 100;

	private Map<TopicPartition, Long> offsetMap = new ConcurrentHashMap<>();
	private Map<TopicPartition, NavigableSet<Long>> offsetDetailMap = new ConcurrentHashMap<>();
	private Map<TopicPartition, ReadWriteLock> lockMap = new ConcurrentHashMap<>();

	public MemoryOffsetManager() {
	}

	@Override
	public void commitSingleThread(TopicPartition partition, long offset) {
		if (logger.isDebugEnabled()) {
			logger.debug("单线程提交：topic: {}, offset: {}", partition, offset);
		}
		offsetMap.put(partition, offset + 1);
	}

	@Override
	public void commitMultiThread(TopicPartition partition, long offset) {
		if (logger.isDebugEnabled()) {
			logger.debug("多线程提交：topic: {}, offset: {}", partition, offset);
		}
		NavigableSet<Long> set = offsetDetailMap.get(partition);
		ReadWriteLock lock = this.getReadWriteLock(partition);
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			if (set == null) {
				set = new TreeSet<>();
				offsetDetailMap.put(partition, set);
			}
			set.add(offset + 1);
		}
		finally {
			writeLock.unlock();
		}
	}

	private ReadWriteLock getReadWriteLock(TopicPartition partition) {
		ReadWriteLock lock = lockMap.get(partition);
		if (lock == null) {
			synchronized (this) {
				lock = lockMap.get(partition);
				if (lock == null) {
					lock = new ReentrantReadWriteLock();
					lockMap.put(partition, lock);
				}
			}
		}
		return lock;
	}

	@Override
	public void mergeMultiThread(Collection<TopicPartition> partitions) {
		if (partitions == null || partitions.isEmpty()) {
			return;
		}

		for (TopicPartition partition : partitions) {
			this.mergeMultiThread(partition);
		}
	}

	@Override
	public void mergeMultiThread(TopicPartition partition) {
		NavigableSet<Long> set = offsetDetailMap.get(partition);
		if (set == null || set.isEmpty()) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("需要合并多线程：topic: {}, offset: {}", partition, set);
		}
		Long currentOffset = offsetMap.get(partition);
		Long minOffset = set.first();
		if (currentOffset != null && currentOffset.longValue() + 1 < minOffset.longValue()) {
			if (logger.isDebugEnabled()) {
				logger.debug("当前 Offset [{}] 小于最小 Offset [{}]，不需要合并", currentOffset, minOffset);
			}
			return;
		}

		List<Long> list = new ArrayList<>();
		ReadWriteLock lock = this.getReadWriteLock(partition);
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			for (Long offset : set) {
				list.add(offset);
			}
			long offset = this.calculateOffset(list);
			if (offset > 0L) {
				NavigableSet<Long> newSet = set.tailSet(offset, false);
				offsetDetailMap.put(partition, newSet);
				offsetMap.put(partition, offset);
			}
		}
		finally {
			writeLock.unlock();
		}
	}

	@Override
	public Map<TopicPartition, OffsetAndMetadata> getOffsetAndMetadataMap(Collection<TopicPartition> partitions) {
		Map<TopicPartition, Long> map = this.getOffsetMap(partitions);
		Map<TopicPartition, OffsetAndMetadata> metaMap = new HashMap<>();
		if (map == null || map.isEmpty()) {
			return metaMap;
		}

		for (Map.Entry<TopicPartition, Long> entry : map.entrySet()) {
			metaMap.put(entry.getKey(), new OffsetAndMetadata(entry.getValue()));
		}
		return metaMap;
	}

	@Override
	public Map<TopicPartition, Long> getOffsetMap(Collection<TopicPartition> partitions) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取 offset: {}", partitions);
		}
		Map<TopicPartition, Long> map = new HashMap<>();
		if (partitions == null || partitions.isEmpty()) {
			return map;
		}

		for (TopicPartition partition : partitions) {
			this.mergeMultiThread(partition);
			Long offset = offsetMap.get(partition);
			if (offset != null) {
				map.put(partition, offset);
			}
		}

		return map;
	}

	@Override
	public boolean isCommitted(TopicPartition partition, long offset) {
		if (logger.isDebugEnabled()) {
			logger.debug("判断是否已提交， TopicPartition: {}, offset: {}", partition, offset);
		}
		boolean committed = false;
		Long lastOffset = offsetMap.get(partition);
		if (lastOffset != null && offset < lastOffset.longValue()) {
			return true;
		}

		NavigableSet<Long> set = offsetDetailMap.get(partition);
		if (set == null || set.isEmpty()) {
			return false;
		}

		ReadWriteLock lock = this.getReadWriteLock(partition);
		Lock readLock = lock.readLock();
		readLock.lock();
		try {
			committed = set.contains(offset + 1);
		}
		finally {
			readLock.unlock();
		}
		return committed;
	}

	private List<Long> convertToList(TopicPartition partition, NavigableSet<Long> set) {
		ReadWriteLock lock = this.getReadWriteLock(partition);
		Lock readLock = lock.readLock();
		List<Long> list = new ArrayList<>();
		readLock.lock();
		try {
			for (Long offset : set) {
				list.add(offset);
			}
		}
		finally {
			readLock.unlock();
		}
		return list;
	}

	private long calculateOffset(List<Long> list) {
		long offset = 0L;
		int i = 0;
		int minIndex = 0;
		int maxIndex = list.size() - 1;
		if (list.size() - 1 == list.get(maxIndex) - list.get(minIndex)) {
			offset = list.get(maxIndex);
		}
		else {
			int middleIndex = (maxIndex + minIndex) / 2;
			while (minIndex + 1 < maxIndex && i < MAX_DEEP) {
				if (middleIndex - minIndex == list.get(middleIndex) - list.get(minIndex)) {
					minIndex = middleIndex;
				}
				else {
					maxIndex = middleIndex;
				}
				middleIndex = (maxIndex + minIndex) / 2;
				offset = list.get(middleIndex);
				i++;
				if (logger.isDebugEnabled()) {
					logger.debug("寻找 offset 过程，minIndex: {}, maxIndex: {}, middleIndex: {}, offset: {}",
							minIndex, maxIndex, middleIndex, offset);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("找到 offset：{}", offset);
		}
		return offset;
	}
}
