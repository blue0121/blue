package blue.mqtt.internal.core.client;

import org.fusesource.mqtt.client.CallbackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-08
 */
public class ConnectionSet {
    private static Logger logger = LoggerFactory.getLogger(ConnectionSet.class);

    private final Lock readLock;
    private final Lock writeLock;
    private final List<String> idList = new ArrayList<>();
    private final Map<String, CallbackConnection> idConnMap = new HashMap<>();
    private final ConcurrentMap<String, String> topicIdMap = new ConcurrentHashMap<>();
	private final AtomicLong counter = new AtomicLong(1);

	public ConnectionSet() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
	}

	public void connect(String clientId, CallbackConnection connection) {
		writeLock.lock();
		try {
			idList.add(clientId);
			idConnMap.put(clientId, connection);
			logger.info("Add ConnectionSet, clientId: {}", clientId);
		} finally {
			writeLock.unlock();
		}
	}

	public void disconnect(String clientId) {
		writeLock.lock();
		try {
			idList.remove(clientId);
			idConnMap.remove(clientId);
			logger.info("Remove ConnectionSet, clientId: {}", clientId);
		} finally {
			writeLock.unlock();
		}
	}

	public Map.Entry<String, CallbackConnection> getConnection() {
		if (idList.isEmpty()) {
			return null;
		}
		int idx = (int) (counter.getAndIncrement() % idList.size());
		readLock.lock();
		try {
			String clientId = idList.get(idx);
			var conn = idConnMap.get(clientId);
			if (conn == null) {
				return null;
			}
			return Map.entry(clientId, conn);
		} finally {
			readLock.unlock();
		}
	}

	public Map.Entry<String, CallbackConnection> getConnectionByTopic(String topic) {
		String clientId = topicIdMap.get(topic);
		if (clientId == null || clientId.isEmpty()) {
			return null;
		}

		readLock.lock();
		try {
			var conn = idConnMap.get(clientId);
			if (conn == null) {
				return null;
			}
			return Map.entry(clientId, conn);
		} finally {
			readLock.unlock();
		}
	}

	public void subscribe(String topic, String clientId) {
		topicIdMap.put(topic, clientId);
    }

    public void unsubscribe(String topic) {
		topicIdMap.remove(topic);
    }

    public boolean exist(String topic) {
	    return topicIdMap.containsKey(topic);
    }

}
