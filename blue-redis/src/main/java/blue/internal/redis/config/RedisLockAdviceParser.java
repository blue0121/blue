package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.lock.RedisLockAdvice;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class RedisLockAdviceParser extends SimpleBeanDefinitionParser
{
	public RedisLockAdviceParser()
	{
		this.clazz = RedisLockAdvice.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "redisson", "ref-redisson");
	}

}
