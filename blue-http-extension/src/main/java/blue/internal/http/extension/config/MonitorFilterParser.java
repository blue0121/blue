package blue.internal.http.extension.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.extension.monitor.MonitorFilter;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class MonitorFilterParser extends SimpleBeanDefinitionParser
{
	public MonitorFilterParser()
	{
		this.clazz = MonitorFilter.class;
	}

}
