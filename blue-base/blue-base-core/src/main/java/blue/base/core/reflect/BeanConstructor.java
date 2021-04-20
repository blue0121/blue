package blue.base.core.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-20
 */
public interface BeanConstructor extends ExecutableOperation, NameOperation {

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

}
