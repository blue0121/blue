package blue.internal.jms.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.apache.qpid.client.AMQConnection;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 2019-08-04
 */
public class QpidConnectionParser extends SimpleBeanDefinitionParser
{
	public QpidConnectionParser()
	{
		this.clazz = AMQConnection.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		builder.addConstructorArgValue(element.getAttribute("url"));
		builder.addConstructorArgValue(element.getAttribute("username"));
		builder.addConstructorArgValue(element.getAttribute("password"));
		builder.addConstructorArgValue(element.getAttribute("client-id"));
		builder.addConstructorArgValue(element.getAttribute("virtual-host"));
	}
}
