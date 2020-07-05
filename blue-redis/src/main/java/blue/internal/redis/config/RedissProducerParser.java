package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.producer.DefaultRedisProducer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Redis Producer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class RedissProducerParser extends SimpleBeanDefinitionParser
{
	public RedissProducerParser()
	{
		this.clazz = DefaultRedisProducer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "redisson", "ref-redisson");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
	}
}
