package blue.validation.core.annotation;

import blue.validation.core.validator.FieldEqualValidator;
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
@Constraint(validatedBy = FieldEqualValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldEqual {
	String[] fields();

	String message() default "字段值不一致";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
