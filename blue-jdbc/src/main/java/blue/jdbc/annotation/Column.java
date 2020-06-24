package blue.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 普通字段注解
 * 
 * @author zhengj
 * @since 2016年7月6日 1.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column
{
	/**
	 * 映射字段名，默认映射为大小写字段名<br/>
	 * user_id => userId
	 */
	String name() default "";
}
