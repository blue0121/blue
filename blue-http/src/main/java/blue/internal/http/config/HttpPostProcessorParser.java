package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.parser.ControllerPostProcessor;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-09
 */
public class HttpPostProcessorParser extends SimpleBeanDefinitionParser
{
	public HttpPostProcessorParser()
	{
		this.clazz = ControllerPostProcessor.class;
	}

}
