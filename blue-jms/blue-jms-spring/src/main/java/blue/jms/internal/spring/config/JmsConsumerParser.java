package blue.jms.internal.spring.config;

import blue.base.spring.common.ListenerContainerParser;
import blue.jms.internal.spring.bean.JmsConsumerConfig;
import blue.jms.internal.spring.bean.JmsConsumerFactoryBean;
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
public class JmsConsumerParser extends ListenerContainerParser {
	public JmsConsumerParser() {
		this.clazz = JmsConsumerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NodeList configList = element.getElementsByTagNameNS(JmsNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0) {
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), JmsConsumerConfig.class);
		}

		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "defaultType", "default-type");
		this.parse(element, builder, "multiThread", "multi-thread");
		this.parseRef(element, builder, "jmsClient", "ref-jms-client");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "executor", "ref-executor");

		var config = this.getBeanProps(element, parserContext, builder, JmsNamespaceHandler.NS, "config");
		builder.addPropertyValue("prop", config);
	}

	@Override
	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz) {
		this.parse(element, definition, "type", "type");
	}
}
