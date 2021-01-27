package blue.internal.http.handler.parameter;

import blue.http.annotation.BodyContent;
import blue.http.annotation.BodyJson;
import blue.http.annotation.BodyParam;
import blue.http.annotation.Multipart;
import blue.http.annotation.PathVariable;
import blue.http.annotation.QueryParam;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import blue.http.message.WebSocketRequest;
import blue.internal.http.annotation.RequestParamConfig;
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

	private final Map<Class<?>, ParamHandler> annotationHttpMap = new HashMap<>();
	private final Map<Class<?>, ParamHandler> typeHttpMap = new HashMap<>();
	private final Map<Class<?>, ParamHandler> annotationWebSocketMap = new HashMap<>();
	private final Map<Class<?>, ParamHandler> typeWebSocketMap = new HashMap<>();

	public ParameterDispatcher()
	{
		// http
		annotationHttpMap.put(BodyContent.class, new BodyContentParamHandler());
		annotationHttpMap.put(BodyJson.class, new BodyJsonParamHandler());
		annotationHttpMap.put(BodyParam.class, new BodyParamParamHandler());
		annotationHttpMap.put(Multipart.class, new MultipartParamHandler());
		annotationHttpMap.put(PathVariable.class, new PathVariableParamHandler());
		annotationHttpMap.put(QueryParam.class, new QueryParamParamHandler());

		typeHttpMap.put(String.class, new StringParamHandler());
		typeHttpMap.put(UploadFile.class, new UploadFileParamHandler());
		typeHttpMap.put(Request.class, new RequestParamHandler());

		// WebSocket

		typeWebSocketMap.put(WebSocketRequest.class, new WebSocketRequestParamHandler());
	}

	public Object handleParam(RequestParamConfig config, Request request)
	{
		return handleParam(config, request, annotationHttpMap, typeHttpMap);
	}

	public Object handleParam(RequestParamConfig config, WebSocketRequest request)
	{
		return handleParam(config, request, annotationWebSocketMap, typeWebSocketMap);
	}

	@SuppressWarnings("unchecked")
	private Object handleParam(RequestParamConfig config, Object request,
	                           Map<Class<?>, ParamHandler> annotationMap, Map<Class<?>, ParamHandler> typeMap)
	{
		if (config.hasParamAnnotation())
		{
			ParamHandler handler = annotationMap.get(config.getParamAnnotationClazz());
			this.check(handler, config.getParamAnnotationClazz());
			return handler.handle(config, request);
		}
		ParamHandler handler = typeMap.get(config.getParamClazz());
		this.check(handler, config.getParamClazz());
		return handler.handle(config, request);
	}

	private void check(ParamHandler handler, Class<?> clazz)
	{
		if (handler == null)
			throw new NullPointerException("ParamHandler is null, class: " + clazz.getName());
	}

}
