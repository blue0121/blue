package blue.internal.kafka.consumer;

import blue.internal.core.message.AbstractListenerContainer;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.kafka.codec.FastjsonDeserializer;
import blue.internal.kafka.offset.MemoryOffsetManager;
import blue.internal.kafka.offset.OffsetManager;
import blue.kafka.exception.KafkaException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Jin Zheng
 * @since 2020-02-10
 */
public class KafkaListenerContainer extends AbstractListenerContainer
{
    private static Logger logger = LoggerFactory.getLogger(KafkaListenerContainer.class);

    private Properties config;
    private OffsetManager offsetManager;
    private List<ConsumerRunnable> threadList = new ArrayList<>();

	public KafkaListenerContainer()
	{
	}

    @Override
    protected void start()
    {
        for (ConsumerListenerConfig config : configList)
        {
            KafkaListenerConfig listenerConfig = (KafkaListenerConfig) config;
            if (listenerConfig.getOffsetManager() == null)
            {
                listenerConfig.setOffsetManager(offsetManager);
            }
            for (int i = 0; i < listenerConfig.getCount(); i++)
            {
                ConsumerRunnable runnable = new ConsumerThread(this.config, listenerConfig);
                threadList.add(runnable);
                Thread thread = new Thread(runnable);
                thread.start();
            }
            logger.info("Subscription: {}", config.toString());
        }
    }

    @Override
    public void destroy() throws Exception
    {
        for (ConsumerRunnable runnable : threadList)
        {
            runnable.destroy();
        }
        logger.info("Destroy KafkaListenerContainer");
    }

    @Override
    protected void check()
    {
        super.check();

        if (this.config == null)
            throw new KafkaException("KafkaConsumerConfig is null");

        if (!this.config.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG))
        {
            logger.info("config '{}' is empty, default: {}", ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            this.config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        }
        if (!this.config.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG))
        {
            logger.info("config '{}' is empty, default: {}", ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FastjsonDeserializer.class.getName());
            this.config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FastjsonDeserializer.class.getName());
        }

        if (offsetManager == null)
        {
            offsetManager = new MemoryOffsetManager();
            logger.info("OffsetManager is null，default: MemoryOffsetManager");
        }
    }

    public void setConfig(Properties config)
    {
        this.config = config;
    }

    public void setOffsetManager(OffsetManager offsetManager)
    {
        this.offsetManager = offsetManager;
    }
}
