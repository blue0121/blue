package blue.jms.internal.core.consumer;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.message.ConsumerListenerConfig;
import blue.jms.core.JmsType;

/**
 * @author Jin Zheng
 * @since 2019-08-02
 */
public class JmsListenerConfig extends ConsumerListenerConfig {
	protected JmsType type;

	public JmsListenerConfig() {
	}

	@Override
	public void init() {
		super.init();
		AssertUtil.notNull(type, "JmsType");
	}

	@Override
	public String toString() {
		return String.format("JmsListenerConfig[topic=%s, type: %s, multi-thread=%s]",
				topic, type.name(), multiThread);
	}

	public JmsType getType() {
		return type;
	}

	public void setType(JmsType type) {
		this.type = type;
	}
}
