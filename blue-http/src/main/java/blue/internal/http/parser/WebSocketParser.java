package blue.internal.http.parser;

import blue.http.annotation.WebSocket;
import blue.http.exception.HttpServerException;
import blue.http.exception.WebSocketServerException;
import blue.http.message.WebSocketRequest;
import blue.internal.http.annotation.DefaultWebSocketUrlConfig;
import blue.internal.http.annotation.WebSocketConfigCache;
import blue.internal.http.annotation.WebSocketUrlKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketParser
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketParser.class);

	private Set<Class<?>> paramSet = new HashSet<>();
	private String paramString;
	private Set<Class<?>> clazzSet = new HashSet<>();

	private WebSocketConfigCache configCache = WebSocketConfigCache.getInstance();

	private static WebSocketParser instance = new WebSocketParser();

	private WebSocketParser()
	{
		paramSet.add(WebSocketRequest.class);
		paramString = paramSet.toString();
	}

	public static WebSocketParser getInstance()
	{
		return instance;
	}

	public void parse(Class<?> clazz)
	{
		if (clazzSet.contains(clazz))
			return;

		WebSocket annoWebSocket = clazz.getAnnotation(WebSocket.class);
		if (annoWebSocket == null)
			throw new WebSocketServerException(clazz.getName() + " 缺少 @WebSocket 注解");

		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getModifiers() != Modifier.PUBLIC)
				continue;

			if (method.getParameterCount() > 1)
				throw new HttpServerException(method.getName() + " 最多只能有一个参数");

			if (method.getParameterCount() > 0)
			{
				Class<?> paramClazz = method.getParameterTypes()[0];
				if (!paramSet.contains(paramClazz))
					throw new HttpServerException(method.getName() + " 的参数只能是：" + paramString);
			}

			this.parseMethod(method, annoWebSocket);
		}
		clazzSet.add(clazz);
	}

	private void parseMethod(Method method, WebSocket annoWebSocket)
	{
		StringBuilder url = new StringBuilder(32);
		if (!annoWebSocket.url().startsWith(HttpParser.SPLIT))
		{
			url.append(HttpParser.SPLIT);
		}
		url.append(annoWebSocket.url());
		int version = annoWebSocket.version();

		WebSocket annoMethod = method.getAnnotation(WebSocket.class);
		if (annoMethod != null)
		{
			if (!annoMethod.url().isEmpty() && !annoMethod.url().startsWith(HttpParser.SPLIT))
			{
				url.append(HttpParser.SPLIT);
			}
			url.append(annoMethod.url());
			if (annoMethod.version() > 0)
			{
				version = annoMethod.version();
			}
		}

		DefaultWebSocketUrlConfig config = new DefaultWebSocketUrlConfig();
		config.setName(url.toString());
		config.setUrl(url.toString());
		config.setVersion(version);
		config.setMethod(method);
		WebSocketUrlKey key = config.buildKey();

		if (configCache.contains(key))
			throw new WebSocketServerException("已经存在：" + key);

		configCache.put(key, config);
		logger.info("Found WebSocket: {}，{}.{}()", config.getUrl(),
				method.getDeclaringClass().getSimpleName(), method.getName());
	}

}
