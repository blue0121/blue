package blue.internal.jms.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public class JmsNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/jms";

	public JmsNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("qpid-connection", new QpidConnectionParser());
		this.registerBeanDefinitionParser("client", new JmsClientParser());
		this.registerBeanDefinitionParser("producer", new JmsProducerParser());
		this.registerBeanDefinitionParser("consumer", new JmsConsumerParser());
	}
}
