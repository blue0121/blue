package blue.internal.http.parser;

import blue.http.annotation.Http;
import blue.http.annotation.WebSocket;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class ControllerPostProcessor implements BeanPostProcessor, ApplicationContextAware
{
	private static ApplicationContext ctx;

	private HttpParser httpParser = HttpParser.getInstance();
	private WebSocketParser webSocketParser = WebSocketParser.getInstance();

	public ControllerPostProcessor()
	{
	}

	public static <T> T getBean(String name, Class<T> clazz)
	{
		return ctx.getBean(name, clazz);
	}

	public static <T> T getBean(Class<T> clazz)
	{
		return ctx.getBean(clazz);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		Class<?> clazz = bean.getClass();
		if (clazz.isAnnotationPresent(Http.class))
		{
			httpParser.parse(clazz);
		}
		else if (clazz.isAnnotationPresent(WebSocket.class))
		{
			webSocketParser.parse(clazz);
		}
		return bean;
	}

	//@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
	{
		String[] names = beanFactory.getBeanNamesForAnnotation(Http.class);
		for (String name : names)
		{
			BeanDefinition definition = beanFactory.getBeanDefinition(name);
			try
			{
				Class<?> clazz = Class.forName(definition.getBeanClassName());
				httpParser.parse(clazz);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		ctx = applicationContext;
	}
}
