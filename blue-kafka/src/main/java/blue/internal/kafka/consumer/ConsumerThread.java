package blue.internal.kafka.consumer;

import blue.internal.kafka.handler.HandleListener;
import blue.internal.kafka.handler.MultiThreadHandlerListener;
import blue.internal.kafka.handler.SingleThreadHandleListener;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

/**
 * 每个线程一个Kafka Consumer
 *
 * @author Jin Zheng
 * @since 1.0 2019-02-28
 */
public class ConsumerThread implements ConsumerRunnable
{
	private static Logger logger = LoggerFactory.getLogger(ConsumerThread.class);

	private KafkaListenerConfig listenerConfig;
	private Properties config;
	private Consumer<String, Object> consumer;
	private HandleListener handleListener;

	public ConsumerThread(Properties config, KafkaListenerConfig listenerConfig)
	{
		this.config = config;
		this.listenerConfig = listenerConfig;
	}

	private void start()
	{
		String group = listenerConfig.getGroup();
		if (group != null && !group.isEmpty())
		{
			config.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		}
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		consumer = new KafkaConsumer<>(config);
		this.createHandleListener();
		consumer.subscribe(listenerConfig.getTopicPattern(), handleListener);
		logger.info("Start Kafka Consumer thread and subscribe，topic: {}, group: {}",
				listenerConfig.getTopic(), group);
	}

	@Override
	public void run()
	{
		try
		{
			this.start();
			while (true)
			{
				ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(listenerConfig.getDuration()));
				if (!records.isEmpty())
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Pending ConsumerRecord: {}", records.count());
					}
					handleListener.handleRecords(records);
					handleListener.commitSync();
				}
			}
		}
		catch (WakeupException e)
		{
			logger.info("Kafka Consumer thread wakeup");
			handleListener.commitSync();
		}
		catch (Exception e)
		{
			logger.error("Error, ", e);
		}
		finally
		{
			if (consumer != null)
			{
				consumer.close();
			}
			logger.info("Stop Kafka Consumer thread");
		}
	}

	@Override
	public void destroy()
	{
		if (consumer != null)
		{
			consumer.wakeup();
		}
	}


	private void createHandleListener()
	{
		if (listenerConfig.isMultiThread())
		{
			handleListener = new MultiThreadHandlerListener(consumer, listenerConfig);
			logger.info("multi thread");
		}
		else
		{
			handleListener = new SingleThreadHandleListener(consumer, listenerConfig);
			logger.info("single thread");
		}
	}

}
