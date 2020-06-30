package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class SslConfigParser extends SimpleBeanDefinitionParser
{
	public SslConfigParser()
	{
		this.clazz = SslConfig.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "enable", "enable");
		this.parse(element, builder, "jksPath", "jks-path");
		this.parse(element, builder, "password", "password");
		this.parse(element, builder, "alias", "alias");
	}
}
