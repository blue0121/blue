package blue.internal.core.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.core.http.DefaultHttpInvoker;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2020-04-25
 */
public class HttpInvokerParser extends SimpleBeanDefinitionParser
{
	public HttpInvokerParser()
	{
		this.clazz = DefaultHttpInvoker.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		Map<Object, Object> map = this.getBeanMap(element, parserContext, builder, CoreNamespaceHandler.NS, "default-headers");
		builder.addPropertyValue("defaultHeaders", map);
		this.parse(element, builder, "baseUrl", "base-url");
		this.parse(element, builder, "proxy", "proxy");
		this.parse(element, builder, "timeout", "timeout");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
	}
}
