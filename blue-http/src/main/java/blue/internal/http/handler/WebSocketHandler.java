package blue.internal.http.handler;

import blue.http.message.WebSocketRequest;
import blue.http.message.WebSocketResponse;
import blue.internal.http.handler.parameter.ParameterDispatcher;
import blue.internal.http.message.DefaultWebSocketResponse;
import blue.internal.http.parser.WebSocketMethodResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class WebSocketHandler implements Handler<WebSocketRequest, WebSocketResponse>
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	private ParameterDispatcher parameterDispatcher;

	public WebSocketHandler(ParameterDispatcher parameterDispatcher)
	{
		this.parameterDispatcher = parameterDispatcher;
	}

	@Override
	public HandlerResponse handle(WebSocketRequest request, HandlerChain chain)
	{
		WebSocketMethodResult result = (WebSocketMethodResult) chain.getHandler();
		DefaultWebSocketResponse response = new DefaultWebSocketResponse();
		Exception ex = null;
		try
		{
			if (!chain.applyPreHandle(request, response))
			{
				return null;
			}
			this.invoke(request, response, result);
			chain.applyPostHandle(request, response);
		}
		catch (Exception e)
		{
			ex = e;
		}
		finally
		{
			chain.triggerAfterCompletion(request, response, ex);
		}
		return HandlerResponse.from(response);
	}

	@Override
	public WebSocketResponse response(HandlerResponse response)
	{
		WebSocketResponse resp = (WebSocketResponse) response.getResponse();
		resp.setResult(response.getResult());

		return resp;
	}

	private void invoke(WebSocketRequest request, DefaultWebSocketResponse response, WebSocketMethodResult result)
	{
		Method method = result.getMethod();
		Object object = result.getTarget();

		Class<?>[] paramClasses = method.getParameterTypes();
		Object[] params = new Object[paramClasses.length];
		for (int i = 0; i < paramClasses.length; i++)
		{
			params[i] = parameterDispatcher.handleParameter(paramClasses[i], request);
		}

		try
		{
			Object rs = method.invoke(object, params);
			response.setResult(rs);
		}
		catch (Throwable e)
		{
			if (e instanceof InvocationTargetException)
				e = e.getCause();

			response.setCause(e);
		}
	}

}
