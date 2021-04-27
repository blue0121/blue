package blue.base.internal.core.message;

import blue.base.core.message.Producer;
import blue.base.core.message.ProducerListener;
import blue.base.core.message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-06
 */
public abstract class AbstractProducer<T extends Topic> implements Producer<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractProducer.class);

	protected String name;
	protected ProducerListener<T, Object> listener;

	public AbstractProducer() {
	}

	public void init() throws Exception {
		if (this.listener == null) {
			this.listener = new LoggerProducerListener<>();
			logger.info("Producer '{}' default ProducerListener is null, use LoggerProducerListener", name);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setProducerListener(ProducerListener<T, Object> listener) {
		this.listener = listener;
	}
}
