package blue.base.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2020-04-25
 */
public class HttpClientParser extends SimpleBeanDefinitionParser {
	public HttpClientParser() {
		this.clazz = HttpClientFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		Map<Object, Object> map = this.getBeanMap(element, parserContext, builder, CoreNamespaceHandler.NS, "default-headers");
		builder.addPropertyValue("defaultHeaders", map);
		this.parse(element, builder, "id", "id");
		this.parse(element, builder, "baseUrl", "base-url");
		this.parse(element, builder, "proxy", "proxy");
		this.parse(element, builder, "timeout", "timeout");
		this.parse(element, builder, "username", "username");
		this.parse(element, builder, "password", "password");
		this.parseRef(element, builder, "executor", "ref-executor");
	}
}
