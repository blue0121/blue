package blue.validation.core.annotation;

import blue.validation.core.validator.IdListValidator;
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
@Constraint(validatedBy = IdListValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdList {
	String message() default "无效的ID列表";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
