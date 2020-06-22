package blue.validation.annotation;


import blue.internal.validation.WordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= WordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Word
{
	String message() default "无效的英文或数字";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
