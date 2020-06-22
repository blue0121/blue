package blue.internal.core.config;


import blue.core.common.SimpleBeanDefinitionParser;
import blue.core.security.SpringBean;

/**
 * @author Jin Zheng
 * @since 2020-02-21
 */
public class SpringBeanParser extends SimpleBeanDefinitionParser
{
	public SpringBeanParser()
	{
		this.clazz = SpringBean.class;
	}
}
