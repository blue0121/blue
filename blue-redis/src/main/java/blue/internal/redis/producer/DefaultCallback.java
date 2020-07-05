package blue.internal.redis.producer;

import blue.core.message.ProducerListener;
import blue.core.message.Topic;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-15
 */
public class DefaultCallback implements BiConsumer<Long, Throwable>
{
	private CountDownLatch latch;
	private Topic topic;
	private Object message;
	private List<ProducerListener<Topic, Object>> listenerList;

	public DefaultCallback(CountDownLatch latch, Topic topic, Object message, List<ProducerListener<Topic, Object>> listenerList)
	{
		this.latch = latch;
		this.topic = topic;
		this.message = message;
		this.listenerList = listenerList;
	}

	@Override
	public void accept(Long k, Throwable e)
	{
		if (e == null)
		{
			for (ProducerListener<Topic, Object> l : listenerList)
			{
				l.onSuccess(topic, message);
			}
		}
		else
		{
			for (ProducerListener<Topic, Object> l : listenerList)
			{
				l.onFailure(topic, message, e);
			}
		}
		if (latch != null)
		{
			latch.countDown();
		}
	}
}
