package blue.validation.annotation;


import blue.internal.validation.validator.NumberInValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= NumberInValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NumberIn
{
	String message() default "无效的数字";
	long[] in();
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
