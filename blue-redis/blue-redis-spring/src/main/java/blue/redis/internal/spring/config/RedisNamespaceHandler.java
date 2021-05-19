package blue.redis.internal.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class RedisNamespaceHandler extends NamespaceHandlerSupport {
	public static final String NS = "http://blue.com/schema/redis";

	public RedisNamespaceHandler() {
	}

	@Override
	public void init() {
		this.registerBeanDefinitionParser("fastjson-codec", new FastjsonCodecParser());
		this.registerBeanDefinitionParser("client", new RedisClientParser());
		this.registerBeanDefinitionParser("producer", new RedisProducerParser());
		this.registerBeanDefinitionParser("consumer", new RedisConsumerParser());
		this.registerBeanDefinitionParser("lock", new RedisLockParser());
		this.registerBeanDefinitionParser("lock-advice", new RedisLockAdviceParser());
		this.registerBeanDefinitionParser("sequence", new RedisSequenceParser());
		this.registerBeanDefinitionParser("cache-manager", new RedisCacheManagerParser());
		this.registerBeanDefinitionParser("cache", new RedisCacheParser());
	}
}
