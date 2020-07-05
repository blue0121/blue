package blue.internal.redis.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class RedisNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/redis";

	public RedisNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("fastjson-codec", new FastjsonCodecParser());
		this.registerBeanDefinitionParser("producer", new RedissProducerParser());
		this.registerBeanDefinitionParser("listener-container", new RedisListenerContrainerParser());
		this.registerBeanDefinitionParser("lock-service", new DefaultLockServiceParser());
		this.registerBeanDefinitionParser("atomic-sequence", new AtomicSequenceParser());
		this.registerBeanDefinitionParser("date-sequence", new DateSequenceParser());
		this.registerBeanDefinitionParser("reset-sequence", new ResetSequenceParser());
	}
}