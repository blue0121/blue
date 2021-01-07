package blue.internal.kafka.config;

import blue.core.common.ListenerContainerParser;
import blue.internal.kafka.consumer.DefaultKafkaConsumer;
import blue.internal.kafka.consumer.KafkaListenerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Kafka 监听器容器解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-11
 */
public class KafkaConsumerParser extends ListenerContainerParser
{
	public KafkaConsumerParser()
	{
		this.clazz = DefaultKafkaConsumer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		NodeList configList = element.getElementsByTagNameNS(KafkaNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0)
		{
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), KafkaListenerConfig.class);
		}

		this.parse(element, builder, "name", "id");
		this.parseRef(element, builder, "config", "ref-config");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
		this.parseRef(element, builder, "offsetManager", "ref-offset-manager");
	}

	@Override
	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz)
	{
		this.parse(element, definition, "count", "count");
		this.parse(element, definition, "group", "group");
		this.parse(element, definition, "duration", "duration");
		this.parseRef(element, definition, "offsetManager", "ref-offset-manager");
	}
}
