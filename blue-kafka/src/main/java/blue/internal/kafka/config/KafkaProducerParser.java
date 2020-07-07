package blue.internal.kafka.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.kafka.producer.DefaultKafkaProducer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Kafka Producer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-11
 */
public class KafkaProducerParser extends SimpleBeanDefinitionParser
{
	public KafkaProducerParser()
	{
		this.clazz = DefaultKafkaProducer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "defaultListener", "ref-default-listener");
		this.parseRef(element, builder, "config", "ref-config");
	}
}
