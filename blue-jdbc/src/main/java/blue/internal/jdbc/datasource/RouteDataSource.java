package blue.internal.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-21
 */
public class RouteDataSource extends AbstractRoutingDataSource
{
	public RouteDataSource()
	{
	}

	/**
	 * 获取与数据源相关的key<br/>
	 * 此key是Map<String,DataSource> resolvedDataSources 中与数据源绑定的key值<br/>
	 * 在通过determineTargetDataSource获取目标数据源时使用
	 */
	@Override
	protected Object determineCurrentLookupKey()
	{
		return RouteHolder.get();
	}
}
