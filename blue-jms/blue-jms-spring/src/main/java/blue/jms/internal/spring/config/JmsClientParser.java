package blue.jms.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.jms.internal.spring.bean.JmsClientFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class JmsClientParser extends SimpleBeanDefinitionParser {
	public JmsClientParser() {
		this.clazz = JmsClientFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "broker", "broker");
		this.parse(element, builder, "type", "type");
		this.parse(element, builder, "username", "username");
		this.parse(element, builder, "password", "password");
		this.parse(element, builder, "clientId", "client-id");
		this.parse(element, builder, "virtualHost", "virtual-host");
	}
}
