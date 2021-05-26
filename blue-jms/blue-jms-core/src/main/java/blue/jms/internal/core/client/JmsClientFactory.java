package blue.jms.internal.core.client;

import blue.base.core.util.AssertUtil;
import blue.jms.core.JmsClient;
import blue.jms.core.options.JmsClientOptions;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class JmsClientFactory {
	private JmsClientFactory() {
	}

	public static JmsClient create(JmsClientOptions options) {
		AssertUtil.notNull(options, "Jms Client Options");
		options.check();
		switch (options.getType()) {
			case QPID:
				return new QpidJmsClient(options);
			case ACTIVE_MQ:
				return new ActiveMQJmsClient(options);
			default:
				throw new UnsupportedOperationException("Unsupported connection type: " + options.getType());
		}
	}

}
