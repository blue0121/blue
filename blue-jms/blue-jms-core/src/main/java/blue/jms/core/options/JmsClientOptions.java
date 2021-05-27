package blue.jms.core.options;

import blue.base.core.message.ClientOptions;
import blue.base.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class JmsClientOptions extends ClientOptions {
	private JmsConnectionType type;
	private String username;
	private String password;
	private String clientId;
	private String virtualHost;

	public JmsClientOptions() {
	}

	@Override
	public void check() {
		super.check();
		AssertUtil.notNull(type, "Jms Connection Type");
		AssertUtil.notEmpty(username, "Username");
		AssertUtil.notEmpty(password, "Password");
	}

	public JmsConnectionType getType() {
		return type;
	}

	public JmsClientOptions setType(JmsConnectionType type) {
		this.type = type;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public JmsClientOptions setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public JmsClientOptions setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public JmsClientOptions setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public JmsClientOptions setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
		return this;
	}
}
