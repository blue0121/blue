package blue.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Http url query string 参数注解
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface QueryParam
{
	/**
	 * 参数名称
	 * @return
	 */
	String value();

	/**
	 * 是否必须有值
	 * @return
	 */
	boolean required() default true;
}
