package blue.internal.kafka.handler;

import blue.internal.kafka.consumer.KafkaListenerConfig;
import blue.kafka.KafkaTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Kafka consumer 多线程处理器
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-11
 */
public class MultiThreadHandlerListener extends AbstractHandleListener
{
	private static Logger logger = LoggerFactory.getLogger(MultiThreadHandlerListener.class);
	
	public MultiThreadHandlerListener(Consumer<String, Object> consumer, KafkaListenerConfig config)
	{
		super(consumer, config);
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions)
	{
		offsetManager.mergeMultiThread(partitions);
		super.onPartitionsRevoked(partitions);
	}

	@Override
	public void handleRecords(ConsumerRecords<String, Object> records)
	{
		final CountDownLatch latch = new CountDownLatch(records.count());
		Set<TopicPartition> partitions = records.partitions();
		for (TopicPartition partition : partitions)
		{
			for (ConsumerRecord<String, Object> record : records.records(partition))
			{
				taskExecutor.execute(() ->
				{
					KafkaTopic topic = new KafkaTopic(record.topic(), record.partition(), record.key());
					try
					{
						if (offsetManager.isCommitted(partition, record.offset()))
						{
							logger.warn("Consumed，topic: {}", topic);
							return;
						}
						consumerListener.onReceive(topic, record.value());
						offsetManager.commitMultiThread(partition, record.offset());
						if (logger.isDebugEnabled())
						{
							logger.debug("Complete ConsumerRecord：{}", record);
						}
					}
					catch (Exception e)
					{
						exceptionHandler.onError(topic, record.value(), e);
					}
					finally
					{
						latch.countDown();
					}
				});
			}
		}
		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			logger.error("被中断：", e);
		}
		offsetManager.mergeMultiThread(partitions);
	}
}
