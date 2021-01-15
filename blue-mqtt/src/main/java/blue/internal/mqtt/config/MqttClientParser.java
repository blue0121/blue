package blue.internal.mqtt.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.mqtt.producer.MqttClient;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * MQTT Template 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class MqttClientParser extends SimpleBeanDefinitionParser
{
	public MqttClientParser()
	{
		this.clazz = MqttClient.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "name", "id");
		this.parse(element, builder, "broker", "broker");
		this.parse(element, builder, "username", "username");
		this.parse(element, builder, "password", "password");
		this.parse(element, builder, "clientId", "client-id");
		this.parse(element, builder, "count", "count");
		this.parse(element, builder, "timeout", "timeout");
		this.parse(element, builder, "keepAlive", "keep-alive");
		this.parse(element, builder, "reconnectDelay", "reconnect-delay");
		this.parseRef(element, builder, "sslContext", "ref-ssl-context");
	}
}
