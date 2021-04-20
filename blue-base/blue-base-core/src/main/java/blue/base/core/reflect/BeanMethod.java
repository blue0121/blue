package blue.base.core.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public interface BeanMethod extends ExecutableOperation {

	/**
	 * return class
	 *
	 * @return
	 */
	Class<?> getReturnClass();

	/**
	 * invoke method with target object, and throw exception
	 *
	 * @param params
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	Object invoke(Object... params) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * invoke method with target object, when exception return null
	 *
	 * @param params
	 * @return
	 */
	Object invokeQuietly(Object... params);

	/**
	 * represent field
	 *
	 * @return
	 */
	String getRepresentField();

	/**
	 * is setter method
	 *
	 * @return
	 */
	boolean isSetter();

	/**
	 * is getter method
	 *
	 * @return
	 */
	boolean isGetter();

}
