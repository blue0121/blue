package blue.jms.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.jms.internal.spring.bean.JmsProducerFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Kafka Producer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-11
 */
public class JmsProducerParser extends SimpleBeanDefinitionParser {
	public JmsProducerParser() {
		this.clazz = JmsProducerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "jmsClient", "ref-jms-client");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
		this.parseRef(element, builder, "executor", "ref-executor");

		var config = this.getBeanProps(element, parserContext, builder, JmsNamespaceHandler.NS, "config");
		builder.addPropertyValue("prop", config);
	}
}
