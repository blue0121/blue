package blue.jms.core.options;

import blue.base.core.message.ProducerOptions;
import blue.base.core.util.AssertUtil;

import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-26
 */
public class JmsProducerOptions extends ProducerOptions {
	private Executor executor;

	public JmsProducerOptions() {
	}

	@Override
	public void check() {
		super.check();
		AssertUtil.notNull(executor, "Executor");
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}
