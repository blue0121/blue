package blue.internal.http.ext.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.ext.management.MonitorController;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class MonitorControllerParser extends SimpleBeanDefinitionParser
{
	public MonitorControllerParser()
	{
		this.clazz = MonitorController.class;
	}

}
