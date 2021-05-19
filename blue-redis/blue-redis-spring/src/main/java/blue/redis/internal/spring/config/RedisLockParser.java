package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisLockFactoryBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-19
 */
public class RedisLockParser extends SimpleBeanDefinitionParser {
	public RedisLockParser() {
		this.clazz = RedisLockFactoryBean.class;
	}
}
