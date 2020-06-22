package blue.validation.annotation;

import blue.internal.validation.HkPassportValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= HkPassportValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HkPassport
{
	String message() default "无效的港澳通行证号码";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
