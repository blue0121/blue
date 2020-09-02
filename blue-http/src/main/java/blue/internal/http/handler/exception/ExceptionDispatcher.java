package blue.internal.http.handler.exception;

import blue.core.common.ErrorCode;
import blue.core.common.ErrorCodeException;
import blue.http.exception.HttpServerException;
import blue.internal.http.handler.HandlerResponse;
import com.alibaba.fastjson.JSONException;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.BeansException;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public class  ExceptionDispatcher
{
	private Map<Class<?>, ExceptionHandler> exceptionHandlerMap = new HashMap<>();

	public ExceptionDispatcher()
	{
		this.exceptionHandlerMap.put(ValidationException.class, new ValidationExceptionHandler());
		this.exceptionHandlerMap.put(BeansException.class, new BeansExceptionHandler());
		this.exceptionHandlerMap.put(JSONException.class, new JSONExceptionHandler());
		this.exceptionHandlerMap.put(HttpServerException.class, new HttpServerExceptionHandler());
		this.exceptionHandlerMap.put(ErrorCodeException.class, new ErrorCodeExceptionHandler());
	}

	@SuppressWarnings("unchecked")
	public void handleException(HandlerResponse response)
	{
		ExceptionHandler handler = exceptionHandlerMap.get(response.getCause().getClass());
		if (handler != null)
		{
			handler.handle(response);
			return;
		}
		response.setResult(ErrorCode.ERROR.toJsonString());
		response.setHttpStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
	}

}
