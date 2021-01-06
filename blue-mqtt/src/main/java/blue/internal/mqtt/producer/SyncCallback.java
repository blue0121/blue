package blue.internal.mqtt.producer;

import org.fusesource.mqtt.client.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public class SyncCallback<T> implements Callback<T>
{
	private static Logger logger = LoggerFactory.getLogger(SyncCallback.class);

	private final CountDownLatch latch;

	public SyncCallback(CountDownLatch latch)
	{
		this.latch = latch;
	}

	@Override
	public void onSuccess(T param)
	{
		latch.countDown();
	}

	@Override
	public void onFailure(Throwable cause)
	{
		latch.countDown();
		logger.error("MQTT error, ", cause);
	}
}
