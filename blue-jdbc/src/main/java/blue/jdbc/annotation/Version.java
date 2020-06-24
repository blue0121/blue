package blue.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 乐观锁，版本数据表列注解
 * 
 * @author zhengj
 * @since 1.0 2016年9月9日
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Version
{
	/**
	 * 是否强制使用乐观锁，默认为true
	 * 
	 * @return
	 */
	boolean force() default true;
}
