package blue.core.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface BeanField
{
	/**
	 * field name
	 * @return
	 */
	String getFieldName();

	/**
	 * column name of database table
	 * @return
	 */
	String getColumnName();

	/**
	 * target object
	 * @return
	 */
	Object getTarget();

	/**
	 * 1. invoke getter method<br/>
	 * 2. field.setAccessible(true) & field.get(target)
	 * @return
	 */
	Object getFieldValue();

	/**
	 * 1. invoke setter method<br/>
	 * 2. field.setAccessible(true) & field.set(target, value)
	 * @param value
	 */
	boolean setFieldValue(Object value);

	/**
	 * getter method
	 * @return
	 */
	Method getGetterMethod();

	/**
	 * setter method
	 * @return
	 */
	Method getSetterMethod();

	/**
	 * 1. setter method annotation
	 * 2. field annotation
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

}
