package blue.validation.core.annotation;

import blue.validation.core.validator.PasswordValidator;
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
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {
	String message() default "无效的密码格式";

	PasswordType type() default PasswordType.PLAIN;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
