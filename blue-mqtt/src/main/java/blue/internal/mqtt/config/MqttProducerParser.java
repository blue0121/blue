package blue.internal.mqtt.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.mqtt.producer.DefaultMqttProducer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * MQTT Template 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class MqttProducerParser extends SimpleBeanDefinitionParser
{
	public MqttProducerParser()
	{
		this.clazz = DefaultMqttProducer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "name", "id");
		this.parse(element, builder, "defaultQos", "default-qos");
		this.parseRef(element, builder, "mqttClient", "ref-mqtt-client");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
	}
}
