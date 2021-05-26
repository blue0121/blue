package blue.jms.internal.core.client;

import blue.jms.core.options.JmsClientOptions;
import org.apache.qpid.client.AMQConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class QpidJmsClient extends AbstractJmsClient {
	private static Logger logger = LoggerFactory.getLogger(QpidJmsClient.class);

	public QpidJmsClient(JmsClientOptions options) {
		super(options);
	}

	@Override
	protected void init() {
		try {
			this.connection = new AMQConnection(options.getBroker(), options.getUsername(), options.getPassword(),
					options.getClientId(), options.getVirtualHost());
			this.connection.start();
		}
		catch (Exception e) {
			logger.error("Error,", e);
		}
	}
}
