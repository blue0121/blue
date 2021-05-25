package blue.kafka.internal.spring.config;

import blue.base.spring.common.ListenerContainerParser;
import blue.kafka.internal.spring.bean.KafkaConsumerConfig;
import blue.kafka.internal.spring.bean.KafkaConsumerFactoryBean;
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
public class KafkaConsumerParser extends ListenerContainerParser {
	public KafkaConsumerParser() {
		this.clazz = KafkaConsumerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NodeList configList = element.getElementsByTagNameNS(KafkaNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0) {
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), KafkaConsumerConfig.class);
		}

		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "count", "count");
		this.parse(element, builder, "group", "group");
		this.parse(element, builder, "duration", "duration");
		this.parse(element, builder, "multiThread", "multi-thread");
		this.parse(element, builder, "keyDeserializer", "key-deserializer-class");
		this.parse(element, builder, "valueDeserializer", "value-deserializer-class");
		this.parseRef(element, builder, "kafkaClient", "ref-kafka-client");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "executor", "ref-executor");
		this.parseRef(element, builder, "offsetManager", "ref-offset-manager");

		var config = this.getBeanProps(element, parserContext, builder, KafkaNamespaceHandler.NS, "config");
		builder.addPropertyValue("prop", config);
	}

	@Override
	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz) {
		this.parse(element, definition, "count", "count");
		this.parse(element, definition, "group", "group");
		this.parse(element, definition, "duration", "duration");
		this.parseRef(element, definition, "offsetManager", "ref-offset-manager");
	}
}
