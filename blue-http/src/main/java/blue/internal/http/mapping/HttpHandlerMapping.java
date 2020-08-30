package blue.internal.http.mapping;

import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.http.message.Request;
import blue.internal.http.annotation.FilterCache;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.parser.HttpMethodResult;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HttpHandlerMapping implements HandlerMapping<Request>
{
	private HttpConfigCache httpConfigCache = HttpConfigCache.getInstance();
	private FilterCache filterCache = FilterCache.getInstance();

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
		HttpUrlKey key = new HttpUrlKey(request.getUrl(), request.getHttpMethod());
		HttpMethodResult result = httpConfigCache.getConfig(key);
		if (result == null)
			return null;

		List<Filter<Object, Object>> filterList = filterCache.matchFilter(request.getUrl(), FilterType.HTTP);
		HandlerChain chain = new HandlerChain(result, filterList);
		return chain;
	}
}
