package blue.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * validate parameter
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Validated
{
	/**
	 * validate group
	 * @return
	 */
	Class<?>[] value() default {};

}
