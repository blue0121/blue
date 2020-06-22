package blue.internal.core.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.core.security.EncryptionPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.List;

/**
 * EncryptionPlaceholder 解析
 *
 * @author zhengjin
 * @since 1.0 2018年12月03日
 */
public class EncryptionPlaceholderParser extends SimpleBeanDefinitionParser
{
	public EncryptionPlaceholderParser()
	{
		this.clazz = EncryptionPropertyPlaceholderConfigurer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		List<Object> list = this.getBeanList(element, parserContext, builder, CoreNamespaceHandler.NS, "locations");
		this.parseList(list, element, "locations");
		builder.addPropertyValue("locations", list);
	}

}
