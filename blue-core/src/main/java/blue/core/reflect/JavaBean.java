package blue.core.reflect;

import blue.internal.core.reflect.DefaultJavaBean;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface JavaBean extends AnnotationOperation
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
