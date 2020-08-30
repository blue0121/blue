package blue.internal.http.handler;

import blue.http.message.Request;
import blue.http.message.Response;
import blue.internal.http.handler.parameter.ParameterDispatcher;
import blue.internal.http.message.DefaultResponse;
import blue.internal.http.parser.HttpMethodResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HttpHandler implements Handler<Request, Response>
{
	private static Logger logger = LoggerFactory.getLogger(HttpHandler.class);

	private ParameterDispatcher parameterDispatcher;

	public HttpHandler(ParameterDispatcher parameterDispatcher)
	{
		this.parameterDispatcher = parameterDispatcher;
	}

	@Override
	public HandlerResponse handle(Request request, HandlerChain chain)
	{
		HttpMethodResult result = (HttpMethodResult) chain.getHandler();
		DefaultResponse response = new DefaultResponse();
		response.setCharset(result.getCharset());
		response.setContentType(result.getContentType());
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
	public Response response(HandlerResponse response)
	{
		Response resp = (Response) response.getResponse();
		resp.setResult(response.getResult());
		resp.setHttpStatus(response.getHttpStatus());
		return resp;
	}

	private void invoke(Request request, DefaultResponse response, HttpMethodResult result)
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
