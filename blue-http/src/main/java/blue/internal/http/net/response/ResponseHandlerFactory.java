package blue.internal.http.net.response;

import blue.http.message.Response;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public class ResponseHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(ResponseHandlerFactory.class);
	private static volatile ResponseHandlerFactory instance;

	private List<ResponseHandler> handlerList = new ArrayList<>();

	private ResponseHandlerFactory()
	{
		handlerList.add(new NullResponseHandler());
		handlerList.add(new ViewResponseHandler());
		handlerList.add(new DownloadResponseHandler());
		handlerList.add(new JsonResponseHandler());
	}

	public static ResponseHandlerFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (ResponseHandlerFactory.class)
			{
				if (instance == null)
				{
					instance = new ResponseHandlerFactory();
				}
			}
		}
		return instance;
	}

	public void handle(Channel ch, HttpRequest request, Response response)
	{
		if (response == null)
		{
			logger.warn("Response is null");
			return;
		}

		Object result = response.getResult();
		for (ResponseHandler handler : handlerList)
		{
			if (handler.accepted(result))
			{
				logger.debug("Response result with: {}", handler.getClass().getName());
				handler.handle(ch, request, response);
				break;
			}
		}
	}

}
