package blue.internal.http.parser;

import blue.core.file.FilePath;
import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.http.annotation.WebSocket;
import blue.http.exception.WebSocketServerException;
import blue.internal.http.annotation.DefaultWebSocketUrlConfig;
import blue.internal.http.annotation.RequestParamConfig;
import blue.internal.http.annotation.WebSocketConfigCache;
import blue.internal.http.annotation.WebSocketUrlKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public class WebSocketParser extends AbstractParser
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketParser.class);

	private WebSocketConfigCache configCache = WebSocketConfigCache.getInstance();
	private WebSocket annotationWebSocket;

	public WebSocketParser(JavaBean bean, WebSocket annotationWebSocket)
	{
		super(bean);
		this.annotationWebSocket = annotationWebSocket;
	}

	@Override
	protected void parseMethod(BeanMethod method)
	{
		int version = annotationWebSocket.version();
		String name = annotationWebSocket.name();
		FilePath uriPath = this.rootPath.copy();
		WebSocket annotationMethod = method.getAnnotation(WebSocket.class);
		if (annotationMethod != null)
		{
			uriPath.concat(FilePath.SLASH, annotationMethod.url());
			name = annotationMethod.name();
			if (annotationMethod.version() > 0)
			{
				version = annotationMethod.version();
			}
		}
		List<RequestParamConfig> paramConfigList = PARAM_PARSER_FACTORY.parse(method);
		String url = uriPath.trim();
		DefaultWebSocketUrlConfig config = new DefaultWebSocketUrlConfig();
		config.setName(name.isEmpty() ? url : name);
		config.setUrl(url);
		config.setVersion(version);
		config.setMethod(method);
		config.setParamList(paramConfigList);
		WebSocketUrlKey key = config.buildKey();

		if (configCache.contains(key))
			throw new WebSocketServerException("url 已经存在：" + key);

		configCache.put(key, config);
		logger.info("Found WebSocket: {}，{}.{}()", config.getUrl(),
				bean.getTargetClass().getSimpleName(), method.getName());
	}

	@Override
	protected String getRootPath()
	{
		return annotationWebSocket.url();
	}
}
