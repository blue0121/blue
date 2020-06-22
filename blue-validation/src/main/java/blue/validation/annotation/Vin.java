package blue.validation.annotation;


import blue.internal.validation.VinValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy= VinValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * @author match
 *
 */
public @interface Vin {
	String message() default "无效的车架号";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
}
