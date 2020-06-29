package blue.internal.http.mapping;

import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.http.message.Request;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.parser.HttpMethodResult;
import blue.internal.http.parser.HttpUrlConfig;
import blue.internal.http.parser.ParserCache;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HttpHandlerMapping implements HandlerMapping<Request>
{
	private ParserCache cache = ParserCache.getInstance();

	public HttpHandlerMapping()
	{
	}

	@Override
	public boolean accepted(Object request)
	{
		return request != null && request instanceof Request;
	}

	@Override
	public HandlerChain getHandlerChain(Request request)
	{
		HttpUrlConfig config = new HttpUrlConfig(request.getUrl(), request.getHttpMethod());
		HttpMethodResult result = cache.getConfig(config);
		if (result == null)
			return null;

		List<Filter<Object, Object>> filterList = cache.matchFilter(request.getUrl(), FilterType.HTTP);
		HandlerChain chain = new HandlerChain(result, filterList);
		return chain;
	}
}
