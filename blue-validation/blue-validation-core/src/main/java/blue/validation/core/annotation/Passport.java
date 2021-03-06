package blue.validation.core.annotation;

import blue.validation.core.validator.PassportValidator;
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
@Constraint(validatedBy = PassportValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Passport {
	String message() default "无效的护照号码";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
