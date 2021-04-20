package blue.base.spring.convert;


/**
 * @author Jin Zheng
 * @since 1.0 2020-06-22
 */
public interface ConvertService {

	/*static ConvertService getInstance() {
		return Singleton.get(DefaultConvertService.class);
	}*/

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);

}
