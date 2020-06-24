package blue.internal.jdbc.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.jdbc.datasource.DataSourceAspect;

/**
 * DataSourceAspect解析
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class DataSourceAspectParser extends SimpleBeanDefinitionParser
{
	public DataSourceAspectParser()
	{
		this.clazz = DataSourceAspect.class;
	}

}
