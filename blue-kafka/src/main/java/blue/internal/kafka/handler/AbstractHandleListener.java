package blue.internal.kafka.handler;

import blue.core.message.Topic;
import blue.internal.core.message.ConsumerListener;
import blue.internal.core.message.ExceptionHandler;
import blue.internal.kafka.consumer.KafkaListenerConfig;
import blue.internal.kafka.offset.OffsetManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Kafka consumer 抽象处理器
 *
 * @author Jin Zheng
 * @since 1.0 2019-03-01
 */
public abstract class AbstractHandleListener implements HandleListener
{
	private static Logger logger = LoggerFactory.getLogger(AbstractHandleListener.class);

	protected final Consumer<String, Object> consumer;
	protected final KafkaListenerConfig config;
	protected final OffsetManager offsetManager;
	protected final TaskExecutor taskExecutor;
	protected final ConsumerListener<Topic, Object> consumerListener;
	protected final ExceptionHandler<Topic, Object> exceptionHandler;

	public AbstractHandleListener(Consumer<String, Object> consumer, KafkaListenerConfig config)
	{
		this.consumer = consumer;
		this.config = config;
		this.offsetManager = config.getOffsetManager();
		this.taskExecutor = config.getTaskExecutor();
		this.consumerListener = config.getListener();
		this.exceptionHandler = config.getExceptionHandler();
	}

	@Override
	public void commitSync()
	{
		Set<TopicPartition> partitions = consumer.assignment();
		Map<TopicPartition, OffsetAndMetadata> offsetMap = offsetManager.getOffsetAndMetadataMap(partitions);
		if (!offsetMap.isEmpty())
		{
			consumer.commitSync(offsetMap);
			if (logger.isDebugEnabled())
			{
				logger.debug("Sync commit offset: {}", this.offsetAndMetadataToString(offsetMap));
			}
		}
	}

	@Override
	public void commitAsync()
	{
		Set<TopicPartition> partitions = consumer.assignment();
		Map<TopicPartition, OffsetAndMetadata> offsetMap = offsetManager.getOffsetAndMetadataMap(partitions);
		if (!offsetMap.isEmpty())
		{
			consumer.commitAsync(offsetMap, null);
			if (logger.isDebugEnabled())
			{
				logger.debug("Async commit offset: {}", this.offsetAndMetadataToString(offsetMap));
			}
		}
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Kafka Consumer occur Partitions Revoked");
		}
		Map<TopicPartition, OffsetAndMetadata> offsetMap = offsetManager.getOffsetAndMetadataMap(partitions);
		if (!offsetMap.isEmpty())
		{
			consumer.commitSync(offsetMap);
			logger.info("Occur Partitions Revoked，sync commit offset: {}", this.offsetAndMetadataToString(offsetMap));
		}
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Kafka Consumer occur Partitions Assigned");
		}
		Map<TopicPartition, Long> offsetMap = offsetManager.getOffsetMap(partitions);
		if (!offsetMap.isEmpty())
		{
			for (Map.Entry<TopicPartition, Long> entry : offsetMap.entrySet())
			{
				consumer.seek(entry.getKey(), entry.getValue());
			}
			logger.info("Occur Partitions Assigned，reset offset: {}", offsetMap);
		}
	}

	private String offsetAndMetadataToString(Map<TopicPartition, OffsetAndMetadata> offsetMap)
	{
		if (offsetMap == null || offsetMap.isEmpty())
			return "{}";

		StringBuilder sb = new StringBuilder(64);
		sb.append("{");
		for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsetMap.entrySet())
		{
			sb.append(entry.getKey()).append("=").append(entry.getValue().offset()).append(",");
		}
		sb.replace(sb.length() - 1, sb.length(), "}");
		return sb.toString();
	}

}
