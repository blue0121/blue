package blue.internal.http.parser;

import blue.core.reflect.JavaBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class ControllerPostProcessor implements BeanPostProcessor
{
	private ParserFactory parserFactory = ParserFactory.getInstance();

	public ControllerPostProcessor()
	{
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		Class<?> clazz = AopUtils.getTargetClass(bean);
		JavaBean javaBean = JavaBean.parse(bean, clazz);
		parserFactory.parse(javaBean);
		return bean;
	}

}
