package blue.validation.core.annotation;

import blue.validation.core.validator.FieldNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
@Constraint(validatedBy = FieldNotBlankValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldNotBlank {
	String[] fields();

	String message() default "字段不能全为空";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
