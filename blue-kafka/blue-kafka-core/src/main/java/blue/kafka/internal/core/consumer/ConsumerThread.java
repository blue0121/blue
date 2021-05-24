package blue.kafka.internal.core.consumer;

import blue.kafka.internal.core.handler.HandleListener;
import blue.kafka.internal.core.handler.MultiThreadHandlerListener;
import blue.kafka.internal.core.handler.SingleThreadHandleListener;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 每个线程一个Kafka Consumer
 *
 * @author Jin Zheng
 * @since 1.0 2019-02-28
 */
public class ConsumerThread implements ConsumerRunnable {
	private static Logger logger = LoggerFactory.getLogger(ConsumerThread.class);

	private KafkaListenerConfig config;
	private Properties prop;
	private Consumer<String, Object> consumer;
	private HandleListener handleListener;

	public ConsumerThread(Properties prop, KafkaListenerConfig config) {
		this.prop = prop;
		this.config = config;
	}

	private void start() {
		String group = config.getGroup();
		prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		consumer = new KafkaConsumer<>(prop);
		this.createHandleListener();
		consumer.subscribe(config.getTopicPattern(), handleListener);
		logger.info("Start Kafka Consumer thread and subscribe，topic: {}, group: {}",
				config.getTopic(), group);
	}

	@Override
	public void run() {
		try {
			this.start();
			while (true) {
				ConsumerRecords<String, Object> records = consumer.poll(config.getDuration());
				if (!records.isEmpty()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Pending ConsumerRecord: {}", records.count());
					}
					handleListener.handleRecords(records);
					handleListener.commitSync();
				}
			}
		}
		catch (WakeupException e) {
			logger.info("Kafka Consumer thread wakeup");
			handleListener.commitSync();
		}
		catch (Exception e) {
			logger.error("Error, ", e);
		}
		finally {
			if (consumer != null) {
				consumer.close();
			}
			logger.info("Stop Kafka Consumer thread");
		}
	}

	@Override
	public void destroy() {
		if (consumer != null) {
			consumer.wakeup();
		}
	}


	private void createHandleListener() {
		if (config.isMultiThread()) {
			handleListener = new MultiThreadHandlerListener(consumer, config);
			logger.info("multi thread");
		}
		else {
			handleListener = new SingleThreadHandleListener(consumer, config);
			logger.info("single thread");
		}
	}

}
