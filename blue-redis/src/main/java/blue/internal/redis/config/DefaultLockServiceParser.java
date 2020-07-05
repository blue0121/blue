package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.lock.DefaultLockService;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class DefaultLockServiceParser extends SimpleBeanDefinitionParser
{
	public DefaultLockServiceParser()
	{
		this.clazz = DefaultLockService.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		builder.addConstructorArgReference(element.getAttribute("ref-redisson"));
	}

}
