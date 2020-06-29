package blue.http.filter;

import blue.http.message.Request;
import blue.http.message.Response;

/**
 * HTTP 调用过滤器
 *
 * @author Jin Zheng
 * @since 2020-02-03
 */
public interface HttpFilter extends Filter<Request, Response>
{
	@Override
	default FilterType getFilterType()
	{
		return FilterType.HTTP;
	}
}
