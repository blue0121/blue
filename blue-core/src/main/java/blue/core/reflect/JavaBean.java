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

	static JavaBean parse(Object target, Class<?> targetClass)
	{
		return new DefaultJavaBean(target, targetClass);
	}

	Class<?> getTargetClass();

	Object getTarget();

	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	Map<String, BeanField> getAllFields();

	BeanField getField(String fieldName);

}
