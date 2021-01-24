package blue.internal.http.handler.parameter;

import blue.core.convert.ConvertService;
import blue.core.util.JsonUtil;
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
		if (target == null || config == null)
			return;

		if (config.hasValidAnnotation())
		{
			ValidationUtil.valid(target, config.getValidAnnotation().value());
		}
	}

	default Object convert(RequestParamConfig config, Object src)
	{
		if (src == null)
			return null;

		ConvertService convertService = ConvertService.getInstance();
		if (convertService.canConvert(src.getClass(), config.getParamClazz()))
			return convertService.convert(src, config.getParamClazz());

		if (src instanceof CharSequence)
		{
			Object target = JsonUtil.fromString(src.toString(), config.getParamClazz());
			this.valid(config, target);
			return target;
		}

		return src;
	}
}
