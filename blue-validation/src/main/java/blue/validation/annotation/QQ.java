package blue.validation.annotation;


import blue.internal.validation.validator.QQValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= QQValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QQ
{
	String message() default "无效的QQ号码";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
