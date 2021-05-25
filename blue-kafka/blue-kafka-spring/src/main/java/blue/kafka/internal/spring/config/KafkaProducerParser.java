package blue.kafka.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.kafka.internal.spring.bean.KafkaProducerFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Kafka Producer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-11
 */
public class KafkaProducerParser extends SimpleBeanDefinitionParser
{
	public KafkaProducerParser() {
		this.clazz = KafkaProducerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "kafkaClient", "ref-kafka-client");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
		this.parse(element, builder, "keySerializer", "key-serializer-class");
		this.parse(element, builder, "valueSerializer", "value-serializer-class");

		var config = this.getBeanProps(element, parserContext, builder, KafkaNamespaceHandler.NS, "config");
		builder.addPropertyValue("prop", config);
	}
}
