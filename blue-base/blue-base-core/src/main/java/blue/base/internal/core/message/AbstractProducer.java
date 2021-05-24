package blue.base.internal.core.message;

import blue.base.core.message.Producer;
import blue.base.core.message.ProducerOptions;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public abstract class AbstractProducer<T extends Topic> implements Producer<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractProducer.class);

	protected final ProducerOptions options;

	public AbstractProducer(ProducerOptions options) {
		AssertUtil.notNull(options, "Producer Options");
		options.check();
		this.options = options;
	}

}
