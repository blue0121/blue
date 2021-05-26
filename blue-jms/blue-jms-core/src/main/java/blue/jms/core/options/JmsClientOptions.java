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

	public void setType(JmsConnectionType type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
}
