package blue.kafka.internal.core.offset;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;

/**
 * Kafka Consumer record offset 管理
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-04
 */
public interface OffsetManager {
	/**
	 * 单线程提交 offset
	 *
	 * @param partition
	 * @param offset
	 */
	void commitSingleThread(TopicPartition partition, long offset);

	/**
	 * 多线程提交 offset
	 *
	 * @param partition
	 * @param offset
	 */
	void commitMultiThread(TopicPartition partition, long offset);

	/**
	 * 多线程合并 offset
	 *
	 * @param partition
	 */
	void mergeMultiThread(TopicPartition partition);

	/**
	 * 多线程合并 offset
	 *
	 * @param partitions
	 */
	void mergeMultiThread(Collection<TopicPartition> partitions);

	/**
	 * 获取 offset，单/多线程共用
	 *
	 * @param partitions
	 * @return
	 */
	Map<TopicPartition, OffsetAndMetadata> getOffsetAndMetadataMap(Collection<TopicPartition> partitions);

	/**
	 * 获取 offset，单/多线程共用
	 *
	 * @param partitions
	 * @return
	 */
	Map<TopicPartition, Long> getOffsetMap(Collection<TopicPartition> partitions);

	/**
	 * 判断是否已提交
	 *
	 * @param partition
	 * @param offset
	 * @return
	 */
	boolean isCommitted(TopicPartition partition, long offset);

}
