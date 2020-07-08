package blue.internal.core.http;

import blue.core.http.PathResponse;

import java.net.http.HttpResponse;
import java.nio.file.Path;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public class DefaultPathResponse extends AbstractResponse<Path> implements PathResponse
{
	public DefaultPathResponse(HttpResponse<Path> response)
	{
		super(response);
	}
}
