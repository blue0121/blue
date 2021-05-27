package blue.jms.core.options;

import blue.base.core.message.ConsumerOptions;
import blue.base.core.util.AssertUtil;
import blue.jms.core.JmsType;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class JmsConsumerOptions extends ConsumerOptions {
	private JmsType defaultType;

	public JmsConsumerOptions() {
	}

	@Override
	public void check() {
		super.check();
		AssertUtil.notNull(defaultType, "Default JmsType");
	}

	public JmsType getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(JmsType defaultType) {
		this.defaultType = defaultType;
	}
}
