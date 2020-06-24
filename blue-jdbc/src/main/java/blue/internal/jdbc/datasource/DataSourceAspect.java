package blue.internal.jdbc.datasource;

import blue.jdbc.annotation.JdbcDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-21
 */
public class DataSourceAspect
{
	private static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);
	
	public DataSourceAspect()
	{
	}

	/**
	 * 在dao层方法之前获取datasource对象之前在切面中指定当前线程数据源路由的key
	 */
	public void beforeDaoMethod(JoinPoint point)
	{
		Class<?> clazz = point.getTarget().getClass();
		String key = null;
		JdbcDataSource anno = clazz.getAnnotation(JdbcDataSource.class);
		if (anno != null)
		{
			key = anno.value();
		}
		anno = ((MethodSignature)point.getSignature()).getMethod().getAnnotation(JdbcDataSource.class);
		if (anno != null)
		{
			key = anno.value();
		}
		logger.debug("DataSourceAspect key: {}", key);
		if (key != null && !key.isEmpty())
		{
			RouteHolder.set(key);
		}
	}
}
