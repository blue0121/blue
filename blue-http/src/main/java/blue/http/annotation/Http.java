package blue.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTTP 服务注解
 * 
 * @author zhengj
 * @since 1.0 2017年3月8日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Http
{
	/**
	 * 接口名称
	 * @return
	 */
	String name() default "";

	/**
	 * 接口地址
	 */
	String url();
	
	/**
	 * 请求方法，默认是所有
	 */
	HttpMethod[] method() default {};
	
	/**
	 * 字符集，默认是UTF-8
	 */
	Charset charset() default Charset.UTF_8;

	/**
	 * Content-Type
	 */
	ContentType contentType() default ContentType.AUTO;

}
