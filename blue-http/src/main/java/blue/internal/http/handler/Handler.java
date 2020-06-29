package blue.internal.http.handler;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public interface Handler<T, V>
{
	/**
	 * 调用控制器
	 *
	 * @param request
	 * @param chain
	 * @return
	 */
	HandlerResponse handle(T request, HandlerChain chain);

	/**
	 * 转换响应
	 *
	 * @param response
	 * @return
	 */
	V response(HandlerResponse response);

}
