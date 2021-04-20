package blue.base.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.base.spring.common.SpringBean;

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
