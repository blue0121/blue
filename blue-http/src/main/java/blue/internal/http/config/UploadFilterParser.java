package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.filter.UploadFilter;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-02
 */
public class UploadFilterParser extends SimpleBeanDefinitionParser
{
	public UploadFilterParser()
	{
		this.clazz = UploadFilter.class;
	}

}
