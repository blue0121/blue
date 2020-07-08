package blue.internal.core.http;

import blue.core.http.StringResponse;

import java.net.http.HttpResponse;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public class DefaultStringResponse extends AbstractResponse<String> implements StringResponse
{
	public DefaultStringResponse(HttpResponse<String> response)
	{
		super(response);
	}
}
