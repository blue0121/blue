package blue.validation.core.validator;

import blue.base.core.reflect.BeanField;
import blue.base.core.reflect.JavaBean;
import blue.base.core.reflect.JavaBeanCache;
import blue.validation.core.annotation.FieldNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class FieldNotBlankValidator implements ConstraintValidator<FieldNotBlank, Object> {
	private String[] fields;

	public FieldNotBlankValidator() {
	}

	@Override
	public void initialize(FieldNotBlank anno) {
		this.fields = anno.fields();

		if (fields == null || fields.length == 0) {
			throw new IllegalArgumentException("@FieldNotBlank 字段不能少于1个");
		}
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		if (obj == null) {
			return true;
		}
		JavaBean bean = JavaBeanCache.getJavaBean(obj.getClass());
		var fieldMap = bean.getAllFields();
		for (var field : fields) {
			BeanField beanField = fieldMap.get(field);
			if (beanField == null) {
				throw new ValidationException(field + " 字段不存在");
			}
			Object value = beanField.getFieldValue(obj);
			if (value != null && !"".equals(value)) {
				return true;
			}
		}

		return false;
	}


}
