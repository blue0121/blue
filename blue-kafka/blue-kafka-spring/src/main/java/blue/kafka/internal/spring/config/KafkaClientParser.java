package blue.kafka.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.kafka.internal.spring.bean.KafkaClientFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-25
 */
public class KafkaClientParser extends SimpleBeanDefinitionParser {
	public KafkaClientParser() {
		this.clazz = KafkaClientFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "broker", "broker");
	}
}
