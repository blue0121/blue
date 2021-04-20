package blue.base.core.reflect;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface BeanField extends AnnotationOperation, ColumnNameOperation {

	/**
	 * target object
	 *
	 * @return
	 */
	Object getTarget();

	/**
	 * 1. invoke getter method<br/>
	 * 2. field.setAccessible(true) & field.get(target)
	 *
	 * @return
	 */
	Object getFieldValue();

	/**
	 * 1. invoke setter method<br/>
	 * 2. field.setAccessible(true) & field.set(target, value)
	 *
	 * @param value
	 * @return
	 */
	boolean setFieldValue(Object value);

	/**
	 * getter method
	 *
	 * @return
	 */
	BeanMethod getGetterMethod();

	/**
	 * setter method
	 *
	 * @return
	 */
	BeanMethod getSetterMethod();

	/**
	 * annotation in this element, Getter Method, Field, with super class and interface
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getGetterAnnotation(Class<T> annotationClass);

	/**
	 * annotations in this element, Getter Method, Field, with super class and interface
	 *
	 * @return
	 */
	List<Annotation> getGetterAnnotations();

	/**
	 * annotation in this element, Setter Method, Field, with super class and interface
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getSetterAnnotation(Class<T> annotationClass);

	/**
	 * annotations in this element, Setter Method, Field, with super class and interface
	 *
	 * @return
	 */
	List<Annotation> getSetterAnnotations();

}
