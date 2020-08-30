package blue.internal.http.parser;

import blue.http.annotation.Http;
import blue.http.annotation.WebSocket;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class ControllerPostProcessor implements BeanPostProcessor
{
	private HttpParser httpParser = HttpParser.getInstance();
	private WebSocketParser webSocketParser = WebSocketParser.getInstance();

	public ControllerPostProcessor()
	{
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		Class<?> clazz = AopUtils.getTargetClass(bean);
		if (clazz.isAnnotationPresent(Http.class))
		{
			httpParser.parse(bean, clazz);
		}
		else if (clazz.isAnnotationPresent(WebSocket.class))
		{
			webSocketParser.parse(clazz);
		}
		return bean;
	}

}
