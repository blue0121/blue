package blue.internal.http.mapping;

import blue.internal.http.handler.HandlerChain;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public interface HandlerMapping<T>
{
	/**
	 * 是否接受
	 *
	 * @param request
	 * @return
	 */
	boolean accepted(Object request);

	/**
	 * 获取处理器调用链
	 * @param request
	 * @return
	 */
	HandlerChain getHandlerChain(T request);

}
