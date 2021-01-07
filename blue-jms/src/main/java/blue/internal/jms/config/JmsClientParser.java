package blue.internal.jms.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.jms.producer.JmsClient;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * MQTT Template 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class JmsClientParser extends SimpleBeanDefinitionParser
{
	public JmsClientParser()
	{
		this.clazz = JmsClient.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "name", "id");
		this.parseRef(element, builder, "connection", "ref-connection");
	}
}
