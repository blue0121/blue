package blue.internal.kafka.handler;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Kafka consumer 处理器
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-01
 */
public interface HandleListener extends ConsumerRebalanceListener
{
	void handleRecords(ConsumerRecords<String, Object> records);

	void commitSync();

	void commitAsync();
}
