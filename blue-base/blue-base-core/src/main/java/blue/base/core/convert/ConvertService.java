package blue.base.core.convert;


/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface ConvertService {

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);

}
