package blue.internal.http.filter;

import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;

import java.util.UUID;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-08
 */
public class SessionCookieFilter implements HttpFilter
{
	public static final String SESSION = "session";

	private String sessionKey = SESSION;

	public SessionCookieFilter()
	{
	}

	@Override
	public boolean preHandle(Request request, Response response) throws Exception
	{
		String session = request.getCookie(sessionKey);
		if (session == null || session.isEmpty())
		{
			String value = UUID.randomUUID().toString();
			response.getCookie().put(sessionKey, value);
		}
		return true;
	}

	public void setSessionKey(String sessionKey)
	{
		if (sessionKey == null || sessionKey.isEmpty())
			return;

		this.sessionKey = sessionKey;
	}
}
