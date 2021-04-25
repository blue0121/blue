package blue.validation.core.annotation;

import blue.validation.core.validator.QQValidator;
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
@Constraint(validatedBy = QQValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QQ {
	String message() default "无效的QQ号码";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
