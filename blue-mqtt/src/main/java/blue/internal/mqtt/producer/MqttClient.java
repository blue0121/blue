package blue.internal.mqtt.producer;

import blue.core.id.IdGenerator;
import blue.core.util.AssertUtil;
import blue.core.util.JsonUtil;
import blue.core.util.RandomUtil;
import blue.core.util.WaitUtil;
import blue.internal.core.message.ProducerListener;
import blue.internal.mqtt.consumer.MqttListenerConfig;
import blue.mqtt.MqttTopic;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.net.ssl.SSLContext;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttClient implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(MqttClient.class);
	public static final String RANDOM = "$RANDOM";

	private CallbackConnection connection;
	private DefaultMqttListener listener;
	private String name;
	private String broker;
	private String username;
	private String password;
	private String clientId;
	private int timeout;
	private int keepAliveInterval;
	private int reconnectDelay;
	private SSLContext sslContext;

	public MqttClient()
	{
		this.listener = new DefaultMqttListener();
	}

	public void subscribe(List<MqttListenerConfig> configList)
	{
		if (configList == null || configList.isEmpty())
			return;

		Topic[] topics = new Topic[configList.size()];
		for (int i = 0; i < configList.size(); i++)
		{
			MqttListenerConfig config = configList.get(i);
			this.listener.addMqttListenerConfig(config);
			topics[i] = new Topic(config.getTopic(), config.getQos().toQoS());
		}
		CountDownLatch latch = new CountDownLatch(1);
		connection.subscribe(topics, new SyncCallback<>(latch));
		WaitUtil.await(latch);
		logger.info("MQTT '{}' subscribe: {}", name, Arrays.toString(topics));
	}

	public void unsubscribe(Collection<String> topicList)
	{
		if (topicList == null || topicList.isEmpty())
			return;

		UTF8Buffer[] buffers = new UTF8Buffer[topicList.size()];
		int i = 0;
		for (var topic : topicList)
		{
			buffers[i] = new UTF8Buffer(topic);
			i++;
		}
		CountDownLatch latch = new CountDownLatch(1);
		connection.unsubscribe(buffers, new SyncCallback<>(latch));
		WaitUtil.await(latch);
		logger.info("MQTT '{}' unsubscribe: {}", name, topicList);
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener)
	{
		byte[] payload = JsonUtil.toBytes(message);
		connection.getDispatchQueue().execute(() ->
		{
			connection.publish(topic.getTopic(), payload, topic.getQos().toQoS(), false, new Callback<Void>()
			{
				@Override
				public void onSuccess(Void value)
				{
					if (listener != null)
					{
						listener.onSuccess(topic, message);
					}
				}

				@Override
				public void onFailure(Throwable cause)
				{
					if (listener != null)
					{
						listener.onFailure(topic, message, cause);
					}
				}
			});
		});
	}

	public void disconnect()
	{
		if (connection != null)
		{
			CountDownLatch latch = new CountDownLatch(1);
			connection.disconnect(new Callback<Void>()
			{
				@Override
				public void onSuccess(Void value)
				{
					latch.countDown();
					logger.info("MQTT '{}' disconnected", name);
				}

				@Override
				public void onFailure(Throwable value)
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
							logger.error("MQTT kill error, ", cause);
						}
					});
				}
			});
			WaitUtil.await(latch);
		}
	}

	public void connect()
	{
		this.check();
		MQTT mqtt = new MQTT();
		try
		{
			mqtt.setHost(broker);
		}
		catch (URISyntaxException e)
		{
			logger.error("Error, ", e);
		}
		if (username != null && !username.isEmpty())
		{
			mqtt.setUserName(username);
		}
		if (password != null && !password.isEmpty())
		{
			mqtt.setPassword(password);
		}
		if (keepAliveInterval > 0)
		{
			mqtt.setKeepAlive((short) keepAliveInterval);
		}
		if (reconnectDelay > 0)
		{
			mqtt.setReconnectDelay(reconnectDelay);
			mqtt.setReconnectDelayMax(reconnectDelay);
		}
		if (sslContext != null)
		{
			mqtt.setSslContext(sslContext);
		}
		mqtt.setDispatchQueue(Dispatch.createQueue());

		CountDownLatch latch = new CountDownLatch(1);
		this.connection = mqtt.callbackConnection();
		connection.listener(listener);
		connection.connect(new SyncCallback<>(latch));
		WaitUtil.await(latch);
		logger.info("MQTT '{}' connected: {}", name, broker);
	}

	private void check()
	{
		AssertUtil.notEmpty(broker, "Broker");

		if (sslContext != null)
		{
			logger.info("MQTT use SSL");
		}
		else
		{
			AssertUtil.notEmpty(username, "username");
			AssertUtil.notEmpty(password, "password");
		}

		if (clientId == null || clientId.isEmpty())
		{
			clientId = IdGenerator.uuid12bit();
		}
		if (clientId.contains(RANDOM))
		{
			String random = RandomUtil.rand(RandomUtil.RandomType.UPPER_LOWER_CASE_NUMBER, 10);
			clientId = clientId.replace(RANDOM, random);
		}
		logger.info("MQTT '{}' ClientId: {}", name, clientId);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBroker()
	{
		return broker;
	}

	public void setBroker(String broker)
	{
		this.broker = broker;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public int getKeepAliveInterval()
	{
		return keepAliveInterval;
	}

	public void setKeepAliveInterval(int keepAliveInterval)
	{
		this.keepAliveInterval = keepAliveInterval;
	}

	public int getReconnectDelay()
	{
		return reconnectDelay;
	}

	public void setReconnectDelay(int reconnectDelay)
	{
		this.reconnectDelay = reconnectDelay;
	}

	public SSLContext getSslContext()
	{
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext)
	{
		this.sslContext = sslContext;
	}

	public DefaultMqttListener getListener()
	{
		return listener;
	}

	@Override
	public void destroy() throws Exception
	{
		this.disconnect();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.connect();
	}
}
