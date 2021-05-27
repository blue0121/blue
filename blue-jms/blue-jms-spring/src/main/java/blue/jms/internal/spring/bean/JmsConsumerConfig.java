package blue.jms.internal.spring.bean;

import blue.base.spring.common.ConsumerConfig;
import blue.jms.core.JmsType;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-10
 */
public class JmsConsumerConfig extends ConsumerConfig {
	private JmsType type;

	public JmsConsumerConfig() {
	}

	public JmsType getType() {
		return type;
	}

	public void setType(JmsType type) {
		this.type = type;
	}
}
