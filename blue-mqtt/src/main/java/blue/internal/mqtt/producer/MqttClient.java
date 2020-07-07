package blue.internal.mqtt.producer;

import blue.core.id.IdGenerator;
import blue.core.message.ByteValue;
import blue.core.message.ProducerListener;
import blue.core.util.JsonUtil;
import blue.core.util.WaitUtil;
import blue.internal.mqtt.consumer.MqttListenerConfig;
import blue.mqtt.exception.MqttException;
import blue.mqtt.model.MqttTopic;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttClient implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(MqttClient.class);

	private CallbackConnection connection;
	private DefaultMqttListener listener;
	private String url;
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
		connection.subscribe(topics, new Callback<byte[]>()
		{
			@Override
			public void onSuccess(byte[] value)
			{
				latch.countDown();
				logger.info("MQTT subscribe: {}", Arrays.toString(topics));
			}

			@Override
			public void onFailure(Throwable cause)
			{
				latch.countDown();
				logger.error("MQTT subscribe error, ", cause);
			}
		});
		WaitUtil.await(latch);
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener)
	{
		byte[] payload = message instanceof ByteValue ? ((ByteValue)message).getBytes() : JsonUtil.toBytes(message);
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
					logger.info("MQTT disconnected");
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
							logger.info("MQTT killed");
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
			mqtt.setHost(url);
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
		connection.connect(new Callback<Void>()
		{
			@Override
			public void onSuccess(Void value)
			{
				latch.countDown();
				logger.info("MQTT connected: {}", url);
			}

			@Override
			public void onFailure(Throwable cause)
			{
				latch.countDown();
				logger.error("MQTT connect error, ", cause);
			}
		});
		WaitUtil.await(latch);
	}

	private void check()
	{
		if (url == null || url.isEmpty())
			throw new MqttException("url config is null");

		if (sslContext != null)
		{
			logger.info("MQTT use SSL");
		}
		else
		{
			if (username == null || username.isEmpty())
				throw new MqttException("username config is null");

			if (password == null || password.isEmpty())
				throw new MqttException("password config is null");
		}

		if (clientId == null || clientId.isEmpty())
		{
			clientId = IdGenerator.uuid12bit();
		}
		logger.info("MQTT ClientId: {}", clientId);
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
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
