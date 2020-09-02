package blue.internal.kafka.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class KafkaNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/kafka";

	public KafkaNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("producer", new KafkaProducerParser());
		this.registerBeanDefinitionParser("listener-container", new KafkaListenerContainerParser());
		this.registerBeanDefinitionParser("memory-offset-manager", new KafkaMemoryOffsetManagerParser());
	}
}