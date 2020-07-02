package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.filter.SettingFilter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class SettingFilterParser extends SimpleBeanDefinitionParser
{
	public SettingFilterParser()
	{
		this.clazz = SettingFilter.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		Map<Object, Object> paramMap = this.getBeanMap(element, parserContext, builder, HttpNamespaceHandler.NS, "param-map");
		builder.addPropertyValue("param", paramMap);
	}
}
