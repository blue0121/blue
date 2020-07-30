package blue.core.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface BeanField
{

	String getFieldName();

	String getColumnName();

	Object getTarget();

	Object getFieldValue();

	void setFieldValue(Object value);

	Method getGetterMethod();

	Method getSetterMethod();

	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

}
