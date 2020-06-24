package blue.internal.jdbc.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-21
 */
public class RouteHolder
{
	private static Logger logger = LoggerFactory.getLogger(RouteHolder.class);
	private static ThreadLocal<String> routeKey = new ThreadLocal<>();

	public RouteHolder()
	{
	}

	/**
	 * 获取当前线程的数据源路由的key
	 * 
	 * @return
	 */
	public static String get()
	{
		String key = routeKey.get();
		logger.debug("DataSourceAspect key: {}", key);
		return key;
	}

	/**
	 * 绑定当前线程数据源路由的key 使用完成后必须调用removeRouteKey()方法删除
	 * 
	 * @param key
	 */
	public static void set(String key)
	{
		routeKey.set(key);
	}

	/**
	 * 删除与当前线程绑定的数据源路由的key
	 */
	public static void remove()
	{
		routeKey.remove();
	}

}
