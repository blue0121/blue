package blue.core.convert;

import blue.core.common.Singleton;
import blue.internal.core.convert.DefaultConvertService;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface ConvertService
{

	static ConvertService getInstance()
	{
		return Singleton.get(DefaultConvertService.class);
	}

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);

}
