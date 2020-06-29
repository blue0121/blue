package blue.http.filter;

import blue.http.message.WebSocketRequest;
import blue.http.message.WebSocketResponse;

/**
 * WebSocket 调用过滤器
 *
 * @author Jin Zheng
 * @since 2020-02-03
 */
public interface WebSocketFilter extends Filter<WebSocketRequest, WebSocketResponse>
{
	@Override
	default FilterType getFilterType()
	{
		return FilterType.WEB_SOCKET;
	}
}
