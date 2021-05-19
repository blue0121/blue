package blue.redis.internal.spring.bean;

import blue.redis.core.Lock;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
@Aspect
public class RedisLockAdvice implements InitializingBean {
	private static Logger logger = LoggerFactory.getLogger(RedisLockAdvice.class);

	private RedisClient redisClient;
	private RedisLock lock;

	public RedisLockAdvice() {
	}

	@Pointcut("@annotation(blue.redis.core.Lock)")
	public void aspectjMethod() {
	}

	@Around("aspectjMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
		Class<?> targetClass = pjp.getTarget().getClass();
		Class<?>[] paraTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();
		Method method = targetClass.getMethod(methodName, paraTypes);
		Lock annotation = method.getAnnotation(Lock.class);
		Object[] args = pjp.getArgs();
		String redisKey = this.getKey(annotation.value(), method, args);
		return lock.lock(redisKey, annotation.leaseMillisTime(), () -> {
			try {
				return pjp.proceed(args);
			}
			catch (Throwable cause) {
				throw new IllegalArgumentException(cause);
			}
		});
	}

	private String getKey(String exp, Method method, Object[] args) {
		return exp;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.lock = redisClient.createLock();
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
}
