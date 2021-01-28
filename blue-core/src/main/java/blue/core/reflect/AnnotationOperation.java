package blue.core.reflect;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public interface AnnotationOperation
{

	/**
	 * declared annotation in this element
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass);

	/**
	 * annotation in this element, with super class and interface
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	/**
	 * declared annotations in this element
	 *
	 * @return
	 */
	List<Annotation> getDeclaredAnnotations();

	/**
	 * declared annotations in this element, with super class and interface
	 *
	 * @return
	 */
	List<Annotation> getAnnotations();

}
