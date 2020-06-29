package blue.internal.http.mapping;

import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.http.message.WebSocketRequest;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.parser.ParserCache;
import blue.internal.http.parser.WebSocketMethodResult;
import blue.internal.http.parser.WebSocketUrlConfig;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class WebSocketHandlerMapping implements HandlerMapping<WebSocketRequest>
{
	private ParserCache cache = ParserCache.getInstance();

	public WebSocketHandlerMapping()
	{
	}

	@Override
	public boolean accepted(Object request)
	{
		return request != null && request instanceof WebSocketRequest;
	}

	@Override
	public HandlerChain getHandlerChain(WebSocketRequest request)
	{
		WebSocketUrlConfig config = new WebSocketUrlConfig(request.getUrl(), request.getVersion());
		WebSocketMethodResult result = cache.getConfig(config);
		if (result == null)
			return null;

		List<Filter<Object, Object>> filterList = cache.matchFilter(request.getUrl(), FilterType.WEB_SOCKET);
		HandlerChain chain = new HandlerChain(result, filterList);
		return chain;
	}
}
