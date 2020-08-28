package blue.http.annotation;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public interface HttpUrlConfig
{
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
}
