package blue.core.reflect;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public interface BeanMethod extends AnnotationOperation
{
	/**
	 * method name
	 *
	 * @return
	 */
	String getMethodName();

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


}
