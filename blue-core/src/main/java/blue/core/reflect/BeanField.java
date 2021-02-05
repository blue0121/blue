package blue.core.reflect;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public interface BeanField extends AnnotationOperation, ColumnNameOperation
{

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
	 * @return
	 */
	boolean setFieldValue(Object value);

	/**
	 * getter method
	 * @return
	 */
	BeanMethod getGetterMethod();

	/**
	 * setter method
	 * @return
	 */
	BeanMethod getSetterMethod();

}
