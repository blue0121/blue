package blue.validation.annotation;


import blue.internal.validation.validator.StringInValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= StringInValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringIn
{
	String message() default "无效的字符串";
	String[] in();
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
