package blue.base.internal.core.message;

import blue.base.core.message.Consumer;
import blue.base.core.message.ConsumerOptions;
import blue.base.core.message.Topic;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-04
 */
public abstract class AbstractConsumer<T extends Topic> implements Consumer<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);

	protected final ConsumerOptions options;

	public AbstractConsumer(ConsumerOptions options) {
		AssertUtil.notNull(options, "Consumer Options");
		options.check();
		this.options = options;
	}

}
