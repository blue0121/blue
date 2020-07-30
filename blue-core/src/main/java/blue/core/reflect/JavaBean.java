package blue.core.reflect;

import blue.internal.core.reflect.DefaultJavaBean;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface JavaBean
{
	/**
	 * create JavaBean object
	 * @param target
	 * @param targetClass
	 * @return
	 */
	static JavaBean parse(Object target, Class<?> targetClass)
	{
		return new DefaultJavaBean(target, targetClass);
	}

	/**
	 * target real class
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * target object
	 * @return
	 */
	Object getTarget();

	/**
	 * class annotation
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	/**
	 * get all BeanField
	 * @return
	 */
	Map<String, BeanField> getAllFields();

	/**
	 * get BeanField by field name
	 * @param fieldName
	 * @return
	 */
	BeanField getField(String fieldName);

}
