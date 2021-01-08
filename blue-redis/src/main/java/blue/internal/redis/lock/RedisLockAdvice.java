package blue.internal.redis.lock;

import blue.redis.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-08
 */
@Aspect
public class RedisLockAdvice
{
	private static Logger logger = LoggerFactory.getLogger(RedisLockAdvice.class);

	private RedissonClient redisson;

	public RedisLockAdvice()
	{
	}

	@Pointcut("@annotation(blue.redis.RedisLock)")
	public void aspectjMethod()
	{
	}

	@Around("aspectjMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable
	{
		String methodName = pjp.getSignature().getName();
		Class<?> targetClass = pjp.getTarget().getClass();
		Class<?>[] paraTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();
		Method method = targetClass.getMethod(methodName, paraTypes);
		RedisLock anno = method.getAnnotation(RedisLock.class);
		Object[] args = pjp.getArgs();
		String redisKey = this.getKey(anno.value(), method, args);
		Object result = null;
		RLock lock = null;
		try
		{
			lock = redisson.getLock(redisKey);
			lock.lock(anno.leaseMillisTime(), TimeUnit.MILLISECONDS);
			logger.info("Redis lock, method: {}.{}, key: {}, {} ms",
					targetClass.getSimpleName(), methodName, redisKey, anno.leaseMillisTime());
			result = pjp.proceed(args);
		}
		finally
		{
			if (lock != null)
			{
				lock.unlock();
			}
		}
		return result;
	}

	private String getKey(String exp, Method method, Object[] args)
	{
		return exp;
	}

	public void setRedisson(RedissonClient redisson)
	{
		this.redisson = redisson;
	}
}
