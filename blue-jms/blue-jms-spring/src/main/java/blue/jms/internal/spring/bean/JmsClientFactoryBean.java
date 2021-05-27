package blue.jms.internal.spring.bean;

import blue.jms.core.JmsClient;
import blue.jms.core.options.JmsClientOptions;
import blue.jms.core.options.JmsConnectionType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-27
 */
public class JmsClientFactoryBean implements FactoryBean<JmsClient>, InitializingBean {
	private String id;
	private String broker;
	private JmsConnectionType type;
	private String username;
	private String password;
	private String clientId;
	private String virtualHost;

	private JmsClient jmsClient;

	public JmsClientFactoryBean() {
	}

	@Override
	public JmsClient getObject() throws Exception {
		return jmsClient;
	}

	@Override
	public Class<?> getObjectType() {
		return JmsClient.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		JmsClientOptions options = new JmsClientOptions();
		options.setId(id).setBroker(broker);
		options.setType(type)
				.setUsername(username)
				.setPassword(password)
				.setClientId(clientId)
				.setVirtualHost(virtualHost);
		this.jmsClient = JmsClient.create(options);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public void setType(JmsConnectionType type) {
		this.type = type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
}
