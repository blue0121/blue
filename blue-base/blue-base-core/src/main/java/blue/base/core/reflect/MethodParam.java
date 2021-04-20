package blue.base.core.reflect;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-05
 */
public interface MethodParam extends AnnotationOperation, NameOperation {

	/**
	 * parameter class
	 *
	 * @return
	 */
	Class<?> getParamClass();

}
