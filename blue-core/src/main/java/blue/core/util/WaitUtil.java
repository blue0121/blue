package blue.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-15
 */
public class WaitUtil
{
	private static Logger logger = LoggerFactory.getLogger(WaitUtil.class);

	public static final long DEFAULT_TIMEOUT = 5;
	public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

	private WaitUtil()
	{
	}

	public static void await(CountDownLatch latch)
	{
		await(latch, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
	}

	public static void await(CountDownLatch latch, long timeout, TimeUnit unit)
	{
		if (latch == null)
			return;

		if (timeout <= 0)
		{
			timeout = DEFAULT_TIMEOUT;
		}
		if (unit == null)
		{
			unit = DEFAULT_TIME_UNIT;
		}
		try
		{
			latch.await(timeout, unit);
		}
		catch (InterruptedException e)
		{
			logger.warn("CountDownLatch await timeout");
		}
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			logger.warn("Interrupted");
		}
	}

	public static void sleep(long time, TimeUnit unit)
	{
		try
		{
			Thread.sleep(unit.toMillis(time));
		}
		catch (InterruptedException e)
		{
			logger.warn("Interrupted");
		}

	}

}
