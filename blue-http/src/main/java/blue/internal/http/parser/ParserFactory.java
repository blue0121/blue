package blue.internal.http.parser;

import blue.core.reflect.JavaBean;
import blue.http.annotation.Http;
import blue.http.annotation.WebSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public class ParserFactory
{

	private static ParserFactory instance = new ParserFactory();

	private Map<Class<?>, Parser> parserMap = new HashMap<>();

	private ParserFactory()
	{
	}

	public static ParserFactory getInstance()
	{
		return instance;
	}

	public void parse(JavaBean bean)
	{
		Parser parser = null;
		Http annotationHttp = bean.getAnnotation(Http.class);
		WebSocket annotationWebSocket = bean.getAnnotation(WebSocket.class);
		if (annotationHttp != null)
		{
			parser = new HttpParser(bean, annotationHttp);
		}
		else if (annotationWebSocket != null)
		{
			parser = new WebSocketParser(bean, annotationWebSocket);
		}
		if (parser == null)
		{
			return;
		}

		parserMap.put(bean.getTargetClass(), parser);
		parser.parse();
	}

}
