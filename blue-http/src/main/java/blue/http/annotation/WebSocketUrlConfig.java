package blue.http.annotation;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public interface WebSocketUrlConfig
{
	/**
	 * web socket url name
	 * @return
	 */
	String getName();

	/**
	 * web socket url
	 * @return
	 */
	String getUrl();

	/**
	 * web socket version
	 * @return
	 */
	int getVersion();

	/**
	 * method
	 * @return
	 */
	Method getMethod();
}
