package blue.internal.mqtt.producer;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-14
 */
public class SyncDisconnectCallback implements Callback<Void>
{
	private static Logger logger = LoggerFactory.getLogger(SyncCallback.class);

	private final String name;
	private final CountDownLatch latch;
	private final CallbackConnection connection;

	public SyncDisconnectCallback(String name, CountDownLatch latch, CallbackConnection connection)
	{
		this.name = name;
		this.latch = latch;
		this.connection = connection;
	}

	@Override
	public void onSuccess(Void param)
	{
		latch.countDown();
	}

	@Override
	public void onFailure(Throwable cause)
	{
		connection.kill(new Callback<Void>()
		{
			@Override
			public void onSuccess(Void value)
			{
				latch.countDown();
				logger.info("MQTT '{}' killed", name);
			}

			@Override
			public void onFailure(Throwable cause)
			{
				latch.countDown();
				logger.error("MQTT '{}' kill error, ", name, cause);
			}
		});
	}
}
