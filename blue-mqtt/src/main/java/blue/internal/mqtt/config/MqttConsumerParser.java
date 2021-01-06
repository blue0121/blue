package blue.internal.mqtt.config;

import blue.core.common.ListenerContainerParser;
import blue.internal.mqtt.consumer.DefaultMqttConsumer;
import blue.internal.mqtt.consumer.MqttListenerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * MQTT Consumer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class MqttConsumerParser extends ListenerContainerParser
{
	public MqttConsumerParser()
	{
		this.clazz = DefaultMqttConsumer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		NodeList configList = element.getElementsByTagNameNS(MqttNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0)
		{
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), MqttListenerConfig.class);
		}

		this.parse(element, builder, "name", "id");
		this.parseRef(element, builder, "mqttClient", "ref-mqtt-client");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
	}

	@Override
	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz)
	{
		this.parse(element, definition, "qos", "qos");
	}
}
