package blue.validation.annotation;


import blue.internal.validation.validator.FieldNotEqualValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy= FieldNotEqualValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldNotEqual
{
	String[] fields();
	String message() default "字段值一致";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
