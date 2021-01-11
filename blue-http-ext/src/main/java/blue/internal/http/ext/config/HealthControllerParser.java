package blue.internal.http.ext.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.ext.management.HealthController;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-11
 */
public class HealthControllerParser extends SimpleBeanDefinitionParser
{
	public HealthControllerParser()
	{
		this.clazz = HealthController.class;
	}

}
