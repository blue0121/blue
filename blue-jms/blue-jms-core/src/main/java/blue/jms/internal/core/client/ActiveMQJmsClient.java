package blue.jms.internal.core.client;

import blue.jms.core.options.JmsClientOptions;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class ActiveMQJmsClient extends AbstractJmsClient {
	private static Logger logger = LoggerFactory.getLogger(ActiveMQJmsClient.class);

	public ActiveMQJmsClient(JmsClientOptions options) {
		super(options);
	}

	@Override
	protected void init() {
		var factory = new ActiveMQConnectionFactory(options.getUsername(), options.getPassword(), options.getBroker());
		try {
			this.connection = factory.createConnection();
			this.connection.start();
		}
		catch (JMSException e) {
			logger.error("Error, ", e);
		}

	}
}
