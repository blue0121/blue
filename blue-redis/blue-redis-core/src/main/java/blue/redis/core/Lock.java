package blue.redis.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-19
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
	/**
	 * redis key
	 *
	 * @return
	 */
	String value();

	/**
	 * lease time
	 *
	 * @return
	 */
	long leaseMillisTime() default RedisLock.LEASE_MILLIS;
}