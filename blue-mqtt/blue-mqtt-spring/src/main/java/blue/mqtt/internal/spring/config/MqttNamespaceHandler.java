package blue.mqtt.internal.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class MqttNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/mqtt";

	public MqttNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("client", new MqttClientParser());
		this.registerBeanDefinitionParser("producer", new MqttProducerParser());
		this.registerBeanDefinitionParser("consumer", new MqttConsumerParser());
	}
}
