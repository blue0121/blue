package blue.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键注解
 * 
 * @author zhengj
 * @since 2016年7月6日 1.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id
{
	/**
	 * 主键产生方式 
	 */
	GeneratorType generator() default GeneratorType.AUTO;
}
