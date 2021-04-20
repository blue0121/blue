package blue.base.core.reflect;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-20
 */
public interface ExecutableOperation extends AnnotationOperation, NameOperation {

	/**
	 * parameter class list
	 *
	 * @return
	 */
	List<Class<?>> getParamClassList();

	/**
	 * method parameter list
	 *
	 * @return
	 */
	List<MethodParam> getParamList();

	/**
	 * modifier
	 *
	 * @return
	 */
	int getModifiers();

}
