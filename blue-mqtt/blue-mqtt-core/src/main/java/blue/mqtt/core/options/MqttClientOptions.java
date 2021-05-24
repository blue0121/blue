package blue.mqtt.core.options;

import blue.base.core.id.IdGenerator;
import blue.base.core.message.ClientOptions;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.RandomUtil;
import blue.mqtt.core.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

/**
 * @author Jin Zheng
 * @since 2021-05-03
 */
public class MqttClientOptions extends ClientOptions {
	private static Logger logger = LoggerFactory.getLogger(MqttClientOptions.class);

	public static final String RANDOM = "$RANDOM";
	public static final int MIN_COUNT = 1;
	public static final int MAX_COUNT = 200;

	private String username;
	private String password;
	private String clientId;
	private int count = MIN_COUNT;
	private int timeout;
	private int keepAlive;
	private int reconnectDelay;
	private SSLContext sslContext;

	public MqttClientOptions() {
	}

	@Override
	public void check() {
		if (count < MIN_COUNT || count > MAX_COUNT) {
			throw new MqttException("MQTT count must be between " + MIN_COUNT + " and " + MAX_COUNT);
		}

		if (sslContext != null) {
			logger.info("MQTT use SSL");
		}
		else {
			AssertUtil.notEmpty(username, "Username");
			AssertUtil.notEmpty(password, "Password");
		}

		if (clientId == null || clientId.isEmpty()) {
			clientId = IdGenerator.uuid12bit();
		}
		if (clientId.contains(RANDOM)) {
			String random = RandomUtil.rand(RandomUtil.RandomType.UPPER_LOWER_CASE_NUMBER, 10);
			clientId = clientId.replace(RANDOM, random);
		}
	}

	public String getUsername() {
		return username;
	}

	public MqttClientOptions setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public MqttClientOptions setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public MqttClientOptions setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public int getCount() {
		return count;
	}

	public MqttClientOptions setCount(int count) {
		this.count = count;
		return this;
	}

	public int getTimeout() {
		return timeout;
	}

	public MqttClientOptions setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public int getKeepAlive() {
		return keepAlive;
	}

	public MqttClientOptions setKeepAlive(int keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public int getReconnectDelay() {
		return reconnectDelay;
	}

	public MqttClientOptions setReconnectDelay(int reconnectDelay) {
		this.reconnectDelay = reconnectDelay;
		return this;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public MqttClientOptions setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}
}
