package blue.internal.http.filter;

import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2020-09-01
 */
public class TokenHttpFilter implements HttpFilter
{
	private static Logger logger = LoggerFactory.getLogger(TokenHttpFilter.class);
	public static final String TOKEN = "X-Token";

	private String tokenKey = TOKEN;

	public TokenHttpFilter()
	{
	}

	@Override
	public boolean preHandle(Request request, Response response) throws Exception
	{
		String token = request.getHeader(tokenKey);
		logger.info("Token: {}", token);

		return true;
	}

	public void setTokenKey(String tokenKey)
	{
		if (tokenKey == null || tokenKey.isEmpty())
			return;

		this.tokenKey = tokenKey;
	}
}
