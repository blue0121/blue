package blue.internal.http.handler.parameter;

import blue.internal.http.annotation.RequestParamConfig;
import blue.validation.ValidationUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public interface ParamHandler<T>
{
	/**
	 * 处理注解参数
	 *
	 * @param config
	 * @param request
	 * @return
	 */
	Object handle(RequestParamConfig config, T request);

	/**
	 * 参数检验
	 *
	 * @param config
	 * @param target
	 */
	default void valid(RequestParamConfig config, Object target)
	{
		if (config.hasValidAnnotation())
		{
			ValidationUtil.valid(target, config.getValidAnnotation().value());
		}
	}
}
