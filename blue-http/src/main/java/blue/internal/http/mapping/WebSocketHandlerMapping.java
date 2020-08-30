package blue.internal.http.mapping;

import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.http.message.WebSocketRequest;
import blue.internal.http.annotation.FilterCache;
import blue.internal.http.annotation.WebSocketConfigCache;
import blue.internal.http.annotation.WebSocketUrlKey;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.parser.WebSocketMethodResult;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class WebSocketHandlerMapping implements HandlerMapping<WebSocketRequest>
{
	private WebSocketConfigCache webSocketConfigCache = WebSocketConfigCache.getInstance();
	private FilterCache filterCache = FilterCache.getInstance();

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
		WebSocketUrlKey key = new WebSocketUrlKey(request.getUrl(), request.getVersion());
		WebSocketMethodResult result = webSocketConfigCache.getConfig(key);
		if (result == null)
			return null;

		List<Filter<Object, Object>> filterList = filterCache.matchFilter(request.getUrl(), FilterType.WEB_SOCKET);
		HandlerChain chain = new HandlerChain(result, filterList);
		return chain;
	}
}
