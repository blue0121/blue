package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisLockAdvice;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-19
 */
public class RedisLockAdviceParser extends SimpleBeanDefinitionParser {
	public RedisLockAdviceParser() {
		this.clazz = RedisLockAdvice.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parseRef(element, builder, "redisClient", "ref-redis-client");
	}

}
