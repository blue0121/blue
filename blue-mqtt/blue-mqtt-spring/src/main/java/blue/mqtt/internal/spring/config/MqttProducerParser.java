package blue.mqtt.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.mqtt.internal.spring.bean.MqttProducerFactoryBean;
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
		this.clazz = MqttProducerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "defaultQos", "default-qos");
		this.parseRef(element, builder, "mqttClient", "ref-mqtt-client");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
	}
}
