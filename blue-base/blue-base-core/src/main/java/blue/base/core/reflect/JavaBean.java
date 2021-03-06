package blue.base.core.reflect;

import blue.base.internal.core.reflect.DefaultJavaBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface JavaBean extends AnnotationOperation, ColumnNameOperation {
	/**
	 * create JavaBean reflection object
	 *
	 * @param target
	 * @param targetClass
	 * @return
	 */
	static JavaBean parse(Object target, Class<?> targetClass) {
		return new DefaultJavaBean(target, targetClass);
	}

	/**
	 * create JavaBean reflection object
	 *
	 * @param targetClass
	 * @return
	 */
	static JavaBean parse(Class<?> targetClass) {
		return new DefaultJavaBean(targetClass);
	}

	/**
	 * target real class
	 *
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * target object
	 *
	 * @return
	 */
	Object getTarget();

	/**
	 * get all constructors
	 *
	 * @return
	 */
	List<BeanConstructor> getAllConstructors();

	/**
	 * get constructor by parameter class type
	 *
	 * @param classes
	 * @return
	 */
	BeanConstructor getConstructor(Class<?>... classes);

	/**
	 * new instance for this class, and throw exception
	 *
	 * @param params
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	Object newInstance(Object... params) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * new instance for this class, when exception return null
	 *
	 * @param params
	 * @return
	 */
	Object newInstanceQuietly(Object... params);

	/**
	 * get all methods
	 *
	 * @return
	 */
	List<BeanMethod> getAllMethods();

	/**
	 * get all BeanField
	 *
	 * @return
	 */
	Map<String, BeanField> getAllFields();

	/**
	 * get BeanField by field name
	 *
	 * @param fieldName
	 * @return
	 */
	BeanField getField(String fieldName);

}
