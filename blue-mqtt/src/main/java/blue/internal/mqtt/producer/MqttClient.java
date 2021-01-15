package blue.internal.mqtt.producer;

import blue.core.id.IdGenerator;
import blue.core.util.AssertUtil;
import blue.core.util.RandomUtil;
import blue.internal.core.message.ProducerListener;
import blue.internal.mqtt.consumer.MqttListenerConfig;
import blue.mqtt.MqttException;
import blue.mqtt.MqttTopic;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.net.ssl.SSLContext;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-06-30
 */
public class MqttClient implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(MqttClient.class);
	public static final String RANDOM = "$RANDOM";

	private MqttConnectionContainer container;
	private DefaultMqttListener listener;
	private String name;
	private String broker;
	private String username;
	private String password;
	private String clientId;
	private int count;
	private int timeout;
	private int keepAlive;
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
		this.container.subscribe(topics);
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
		this.container.unsubscribe(buffers);
	}

	public void publish(MqttTopic topic, Object message, ProducerListener<MqttTopic, Object> listener)
	{
		this.container.publish(topic, message, listener);
	}

	public void disconnect()
	{
		this.container.disconnect();
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
		if (keepAlive > 0)
		{
			mqtt.setKeepAlive((short) keepAlive);
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

		this.container = new MqttConnectionContainer(name, count);
		this.container.connect(mqtt, clientId, listener);
	}

	private void check()
	{
		AssertUtil.notEmpty(broker, "Broker");
		if (count < 1 || count > 200)
			throw new MqttException("MQTT count must be between 1 and 200");

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

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public int getKeepAlive()
	{
		return keepAlive;
	}

	public void setKeepAlive(int keepAlive)
	{
		this.keepAlive = keepAlive;
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
