package blue.internal.http.handler.parameter;

import blue.http.message.Request;
import blue.http.message.UploadFile;
import blue.http.message.WebSocketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public class ParameterDispatcher
{
	private static Logger logger = LoggerFactory.getLogger(ParameterDispatcher.class);
	private Map<Class<?>, ParameterHandler<?>[]> handlerMap = new HashMap<>();

	public ParameterDispatcher()
	{
		this.handlerMap.put(String.class, new ParameterHandler[] {new StringParameterHandler()});
		this.handlerMap.put(UploadFile.class, new ParameterHandler[] {new UploadFileParameterHandler()});
		this.handlerMap.put(Request.class, new ParameterHandler[] {new RequestParameterHandler()});
		this.handlerMap.put(WebSocketRequest.class, new ParameterHandler[] {new WebSocketRequestParameterHandler()});
	}

	@SuppressWarnings("unchecked")
	public Object handleParameter(Class<?> clazz, Object request)
	{
		ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) handlerMap.get(clazz);
		if (handlers == null || handlers.length == 0)
		{
			logger.warn("ParameterHandler is null, parameter class: {}", clazz.getName());
			return request;
		}
		for (ParameterHandler<Object> handler : handlers)
		{
			if (handler.accepted(request))
			{
				return handler.handleParameter(request);
			}
		}
		return null;
	}

}
