package blue.internal.jms.config;

import blue.core.common.ListenerContainerParser;
import blue.internal.jms.consumer.JmsListenerConfig;
import blue.internal.jms.consumer.JmsListenerContainer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Jms Listener Container 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class JmsListenerContrainerParser extends ListenerContainerParser
{
	public JmsListenerContrainerParser()
	{
		this.clazz = JmsListenerContainer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		NodeList configList = element.getElementsByTagNameNS(JmsNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0)
		{
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), JmsListenerConfig.class);
		}

		this.parseRef(element, builder, "producer", "ref-jms-producer");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
	}

	@Override
	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz)
	{
		this.parse(element, definition, "type", "type");
	}
}
