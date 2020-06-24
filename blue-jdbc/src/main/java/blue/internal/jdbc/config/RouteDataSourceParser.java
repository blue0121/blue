package blue.internal.jdbc.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.jdbc.datasource.RouteDataSource;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * RouteDataSource解析
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class RouteDataSourceParser extends SimpleBeanDefinitionParser
{
	public RouteDataSourceParser()
	{
		this.clazz = RouteDataSource.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		Map<Object, Object> map = this.getBeanMap(element, parserContext, builder, JdbcNamespaceHandler.NS, "target-data-sources");
		builder.addPropertyValue("targetDataSources", map);
		this.parseRef(element, builder, "defaultDataSource", "default-data-source");
	}

}
