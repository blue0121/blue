package blue.internal.http.extension.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.extension.monitor.MonitorController;

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
