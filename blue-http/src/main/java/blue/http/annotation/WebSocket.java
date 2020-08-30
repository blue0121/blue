package blue.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebSocket 服务注解
 *
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface WebSocket
{
	/**
	 * 名称
	 * @return
	 */
	String name() default "";

	/**
	 * URL
	 */
	String url();

	/**
	 * 版本
	 */
	int version() default 0;
}
