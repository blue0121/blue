package blue.internal.jdbc.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.jdbc.core.JdbcObjectTemplate;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Template解析
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class TemplateParser extends SimpleBeanDefinitionParser
{
	public TemplateParser()
	{
		this.clazz = JdbcObjectTemplate.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parseRef(element, builder, "dataSource", "ref-data-source");
		this.parse(element, builder, "debug", "debug");
		this.parse(element, builder, "escape", "escape");
		this.parse(element, builder, "machineIdProperties", "machine-id-properties");

		List<Object> list = this.getBeanList(element, parserContext, builder, JdbcNamespaceHandler.NS, "scan-packages");
		list = this.parseList(list, element, "scan-packages");
		builder.addPropertyValue("scanPackages", list);
	}

}
