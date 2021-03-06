package blue.internal.http.handler;

import blue.core.reflect.BeanMethod;
import blue.http.message.Request;
import blue.http.message.Response;
import blue.internal.http.annotation.RequestParamConfig;
import blue.internal.http.handler.parameter.ParameterDispatcher;
import blue.internal.http.message.DefaultResponse;
import blue.internal.http.parser.HttpMethodResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
		List<RequestParamConfig> configList = result.getParamList();
		Object[] params = new Object[configList.size()];
		int i = 0;
		for (var config : configList)
		{
			try
			{
				params[i] = parameterDispatcher.handleParam(config, request);
			}
			catch (Exception e)
			{
				response.setCause(e);
				return;
			}
			i++;
		}

		BeanMethod method = result.getMethod();
		try
		{
			Object rs = method.invoke(params);
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
