package blue.redis.core.options;

import blue.base.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-11
 */
public class RedisClientOptions {
    private String id;
    private Object codec;
    private RedisConnectionMode mode = RedisConnectionMode.SINGLE;
    private String broker;
    private String masterName;
    private int database = 0;
    private String password;
    private int timeoutMillis;
	private int subscriptionConnectionPoolSize;
	private int connectionPoolSize;
	private int retry;

	public RedisClientOptions() {
	}

	public void check() {
		AssertUtil.notNull(mode, "RedisConnectionMode");
		AssertUtil.notEmpty(broker, "Broker");
		if (mode == RedisConnectionMode.SENTINEL) {
			AssertUtil.notEmpty(masterName, "MasterName");
		}
		AssertUtil.nonNegative(timeoutMillis, "Timeout");
		AssertUtil.nonNegative(subscriptionConnectionPoolSize, "SubscriptionConnectionPoolSize");
		AssertUtil.nonNegative(connectionPoolSize, "ConnectionPoolSize");
		AssertUtil.nonNegative(retry, "Retry");
	}

	public String getId() {
		return id;
	}

	public RedisClientOptions setId(String id) {
		this.id = id;
		return this;
	}

	public Object getCodec() {
		return codec;
	}

	public RedisClientOptions setCodec(Object codec) {
		this.codec = codec;
		return this;
	}

	public RedisConnectionMode getMode() {
		return mode;
	}

	public RedisClientOptions setMode(RedisConnectionMode mode) {
		this.mode = mode;
		return this;
	}

	public String getBroker() {
		return broker;
	}

	public RedisClientOptions setBroker(String broker) {
		this.broker = broker;
		return this;
	}

	public String getMasterName() {
		return masterName;
	}

	public RedisClientOptions setMasterName(String masterName) {
		this.masterName = masterName;
		return this;
	}

	public int getDatabase() {
		return database;
	}

	public RedisClientOptions setDatabase(int database) {
		this.database = database;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public RedisClientOptions setPassword(String password) {
		this.password = password;
		return this;
	}

	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public RedisClientOptions setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		return this;
	}

	public int getSubscriptionConnectionPoolSize() {
		return subscriptionConnectionPoolSize;
	}

	public RedisClientOptions setSubscriptionConnectionPoolSize(int subscriptionConnectionPoolSize) {
		this.subscriptionConnectionPoolSize = subscriptionConnectionPoolSize;
		return this;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public RedisClientOptions setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
		return this;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}
}
