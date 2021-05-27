package blue.jms.internal.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-29
 */
public class JmsNamespaceHandler extends NamespaceHandlerSupport {
	public static final String NS = "http://blue.com/schema/jms";

	public JmsNamespaceHandler() {
	}

	@Override
	public void init() {
		this.registerBeanDefinitionParser("client", new JmsClientParser());
		this.registerBeanDefinitionParser("producer", new JmsProducerParser());
		this.registerBeanDefinitionParser("consumer", new JmsConsumerParser());
	}
}
