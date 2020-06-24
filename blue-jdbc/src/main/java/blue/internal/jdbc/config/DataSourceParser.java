package blue.internal.jdbc.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * DataSource解析
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class DataSourceParser extends SimpleBeanDefinitionParser
{
	public DataSourceParser()
	{
		this.clazz = BasicDataSource.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "url", "url");
		this.parse(element, builder, "username", "username");
		this.parse(element, builder, "password", "password");
		this.parse(element, builder, "driverClassName", "driver-class-name");
		this.parse(element, builder, "validationQuery", "validation-query");
		this.parse(element, builder, "initialSize", "initial-size");
		this.parse(element, builder, "maxTotal", "max-total");
		this.parse(element, builder, "maxIdle", "max-idle");
		this.parse(element, builder, "minIdle", "min-idle");
		this.parse(element, builder, "maxWaitMillis", "max-wait-millis");

	}

}
