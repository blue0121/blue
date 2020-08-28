package blue.http.annotation;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2020-06-16
 */
public enum HttpMethod
{
	GET,

	POST,

	PUT,

	DELETE,

	PATCH;


	public static Set<HttpMethod> all()
	{
		return Set.of(GET, POST, PUT, DELETE, PATCH);
	}

}
