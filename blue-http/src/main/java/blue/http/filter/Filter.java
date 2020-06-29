package blue.http.filter;

/**
 * 调用过滤器
 *
 * @author Jin Zheng
 * @since 2020-01-05
 */
public interface Filter<T, V>
{
	/**
	 * 预/前处理
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	default boolean preHandle(T request, V response) throws Exception
	{
		return true;
	}

	/**
	 * 后处理
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	default void postHandle(T request, V response) throws Exception
	{
	}

	/**
	 * 完成后处理
	 *
	 * @param request
	 * @param response
	 * @param ex
	 * @throws Exception
	 */
	default void afterCompletion(T request, V response, Exception ex) throws Exception
	{
	}

	/**
	 * 获取过滤器类型
	 *
	 * @return
	 */
	FilterType getFilterType();

}
