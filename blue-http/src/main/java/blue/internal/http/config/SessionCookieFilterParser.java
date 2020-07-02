package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.filter.SessionCookieFilter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class SessionCookieFilterParser extends SimpleBeanDefinitionParser
{
	public SessionCookieFilterParser()
	{
		this.clazz = SessionCookieFilter.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "sessionKey", "session-key");
	}
}
