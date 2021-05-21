package blue.kafka.internal.core.handler;

import blue.kafka.core.KafkaTopic;
import blue.kafka.internal.core.consumer.KafkaListenerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka consumer 单线程处理器
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-01
 */
public class SingleThreadHandleListener extends AbstractHandleListener {
	private static Logger logger = LoggerFactory.getLogger(SingleThreadHandleListener.class);

	public SingleThreadHandleListener(Consumer<String, Object> consumer, KafkaListenerConfig config) {
		super(consumer, config);
	}

	@Override
	public void handleRecords(ConsumerRecords<String, Object> records) {
		for (TopicPartition partition : records.partitions()) {
			for (ConsumerRecord<String, Object> record : records.records(partition)) {
				KafkaTopic topic = new KafkaTopic(record.topic(), record.partition(), record.key());
				try {
					consumerListener.onReceive(topic, record.value());
					offsetManager.commitSingleThread(partition, record.offset());
					if (logger.isDebugEnabled()) {
						logger.debug("Complete ConsumerRecord：{}", record);
					}
				}
				catch (Exception e) {
					exceptionHandler.onError(topic, record.value(), e);
				}
			}
		}

	}

}
