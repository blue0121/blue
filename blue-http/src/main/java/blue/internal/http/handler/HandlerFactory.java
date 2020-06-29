package blue.internal.http.handler;

import blue.core.util.AssertUtil;
import blue.http.message.Request;
import blue.http.message.WebSocketRequest;
import blue.internal.http.handler.exception.ExceptionDispatcher;
import blue.internal.http.handler.parameter.ParameterDispatcher;
import blue.internal.http.parser.HttpMethodResult;
import blue.internal.http.parser.WebSocketMethodResult;
import blue.internal.http.util.HttpLogUtil;
import blue.internal.http.util.WebSocketLogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HandlerFactory
{
	private static volatile HandlerFactory instance;

	private Map<Class<?>, Handler<?, ?>> handlerMap = new HashMap<>();

	private ExceptionDispatcher exceptionDispatcher = new ExceptionDispatcher();
	private ParameterDispatcher parameterDispatcher = new ParameterDispatcher();

	private HandlerFactory()
	{
		this.handlerMap.put(HttpMethodResult.class, new HttpHandler(parameterDispatcher));
		this.handlerMap.put(WebSocketMethodResult.class, new WebSocketHandler(parameterDispatcher));
	}

	public static HandlerFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (HandlerFactory.class)
			{
				if (instance == null)
				{
					instance = new HandlerFactory();
				}
			}
		}
		return instance;
	}

	public <T, V> V handle(T request, HandlerChain chain)
	{
		Class<?> clazz = chain.getHandler().getClass();
		Handler<T, V> handler = (Handler<T, V>) handlerMap.get(clazz);
		AssertUtil.notNull(handler, "Handler");

		HandlerResponse response = handler.handle(request, chain);
		if (response.getCause() != null)
		{
			exceptionDispatcher.handleException(response);
			if (request instanceof Request)
			{
				HttpLogUtil.error((Request)request, response.getResult().toString(), response.getCause());
			}
			else if (request instanceof WebSocketRequest)
			{
				WebSocketLogUtil.error((WebSocketRequest)request, response);
			}
		}
		return handler.response(response);
	}

}
