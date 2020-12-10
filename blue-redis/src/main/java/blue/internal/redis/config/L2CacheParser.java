package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.cache.DefaultL2Cache;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-10
 */
public class L2CacheParser extends SimpleBeanDefinitionParser
{
	public L2CacheParser()
	{
		this.clazz = DefaultL2Cache.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "redisson", "ref-redisson");
		this.parse(element, builder, "keyPrefix", "key-prefix");
		this.parse(element, builder, "ttl", "ttl");
		this.parse(element, builder, "localTtl", "local-ttl");
		this.parse(element, builder, "localMaxSize", "local-max-size");
		this.parse(element, builder, "timeout", "timeout");
	}
}
