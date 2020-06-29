package blue.internal.http.handler.parameter;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public interface ParameterHandler<T>
{
	/**
	 * 是否接受
	 *
	 * @param target
	 * @return
	 */
	boolean accepted(Object target);

	/**
	 * 处理参数
	 *
	 * @param request
	 * @return
	 */
	Object handleParameter(T request);

}
