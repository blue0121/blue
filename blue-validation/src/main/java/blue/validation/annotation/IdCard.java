package blue.validation.annotation;

import blue.internal.validation.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= IdCardValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdCard
{
	String message() default "无效的身份证号码";
	boolean strict() default false;
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
