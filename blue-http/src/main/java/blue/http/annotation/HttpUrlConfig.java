package blue.http.annotation;

import blue.internal.http.annotation.HttpConfigCache;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public interface HttpUrlConfig
{
	/**
	 * all http url config
	 * @return
	 */
	static List<HttpUrlConfig> allConfig()
	{
		return List.copyOf(HttpConfigCache.getInstance().all());
	}

	/**
	 * http url name
	 * @return
	 */
	String getName();

	/**
	 * http url
	 * @return
	 */
	String getUrl();

	/**
	 * http method
	 * @return
	 */
	HttpMethod getHttpMethod();

	/**
	 * http charset
	 * @return
	 */
	Charset getCharset();

	/**
	 * http content-type
	 * @return
	 */
	ContentType getContentType();

	/**
	 * method
	 * @return
	 */
	Method getMethod();

	/**
	 * class instance
	 * @return
	 */
	Object getTarget();
}
