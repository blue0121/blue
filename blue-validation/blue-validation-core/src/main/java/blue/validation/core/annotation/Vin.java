package blue.validation.core.annotation;

import blue.validation.core.validator.VinValidator;
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
@Documented
@Constraint(validatedBy = VinValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * @author match
 *
 */
public @interface Vin {
	String message() default "无效的车架号";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
