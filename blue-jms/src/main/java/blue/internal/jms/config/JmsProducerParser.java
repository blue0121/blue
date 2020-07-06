package blue.internal.jms.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.jms.producer.DefaultJmsProducer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * MQTT Template 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class JmsProducerParser extends SimpleBeanDefinitionParser
{
	public JmsProducerParser()
	{
		this.clazz = DefaultJmsProducer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "connection", "ref-connection");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
	}
}
