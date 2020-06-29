package blue.internal.http.filter;

import blue.core.util.JsonUtil;
import blue.http.filter.WebSocketFilter;
import blue.http.message.WebSocketRequest;
import blue.http.message.WebSocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2020-02-07
 */
public class TokenWebSocketFilter implements WebSocketFilter
{
	private static Logger logger = LoggerFactory.getLogger(TokenWebSocketFilter.class);

	public TokenWebSocketFilter()
	{
	}

	@Override
	public boolean preHandle(WebSocketRequest request, WebSocketResponse response) throws Exception
	{
		logger.info("request: {}", JsonUtil.output(request));
		return true;
	}
}
