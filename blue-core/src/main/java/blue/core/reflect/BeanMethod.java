package blue.core.reflect;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public interface BeanMethod extends AnnotationOperation, NameOperation
{

	/**
	 * parameter class list
	 *
	 * @return
	 */
	List<Class<?>> getParamClassList();

	/**
	 * method parameter list
	 * @return
	 */
	List<MethodParam> getParamList();

	/**
	 * return class
	 *
	 * @return
	 */
	Class<?> getReturnClass();

	/**
	 * modifier
	 *
	 * @return
	 */
	int getModifiers();

	/**
	 * invoke method with target object
	 *
	 * @param params
	 * @return
	 */
	Object invoke(Object...params);

	/**
	 * represent field
	 * @return
	 */
	String getRepresentField();

	/**
	 * is setter method
	 * @return
	 */
	boolean isSetter();

	/**
	 * is getter method
	 * @return
	 */
	boolean isGetter();

}
