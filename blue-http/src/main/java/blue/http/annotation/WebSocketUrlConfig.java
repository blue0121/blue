package blue.http.annotation;

import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.internal.http.annotation.WebSocketConfigCache;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public interface WebSocketUrlConfig
{
	/**
	 * all web socket url config
	 * @return
	 */
	static List<WebSocketUrlConfig> allConfig()
	{
		return List.copyOf(WebSocketConfigCache.getInstance().all());
	}

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
	BeanMethod getMethod();

	/**
	 * class instance
	 * @return
	 */
	JavaBean getJavaBean();
}
