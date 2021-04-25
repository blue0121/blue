package blue.validation.core.validator;

import blue.base.core.reflect.BeanField;
import blue.base.core.reflect.JavaBean;
import blue.base.core.reflect.JavaBeanCache;
import blue.validation.core.annotation.FieldEqual;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class FieldEqualValidator implements ConstraintValidator<FieldEqual, Object> {
	private String[] fields;

	public FieldEqualValidator() {
	}

	@Override
	public void initialize(FieldEqual anno) {
		this.fields = anno.fields();

		if (fields == null || fields.length < 2) {
			throw new IllegalArgumentException("@FieldEqual 验证字段不能少于2个");
		}
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		if (obj == null) {
			return true;
		}

		JavaBean bean = JavaBeanCache.getJavaBean(obj.getClass());
		var fieldMap = bean.getAllFields();

		Object value = null;
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			BeanField beanField = fieldMap.get(field);
			if (beanField == null) {
				throw new ValidationException(field + " 字段不存在");
			}

			Object current = beanField.getFieldValue(obj);
			if (i > 0) {
				if (value == null) {
					if (current != null) {
						return false;
					}
				}
				else {
					if (!value.equals(current)) {
						return false;
					}
				}
			}
			value = current;
		}

		return true;
	}
}
