package blue.base.core.message;

import blue.base.core.id.IdGenerator;
import blue.base.internal.core.message.LoggerProducerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
@SuppressWarnings("rawtypes")
public class ProducerOptions {
	private static Logger logger = LoggerFactory.getLogger(ProducerOptions.class);

	protected String id;
	protected ProducerListener producerListener;

	public ProducerOptions() {
	}

	public void check() {
		if (id == null || id.isEmpty()) {
			id = "Producer-" + IdGenerator.uuid12bit();
		}
		if (producerListener == null) {
			logger.info("Producer '{}' default ProducerListener is null, use LoggerProducerListener", id);
			producerListener = new LoggerProducerListener();
		}
	}

	public String getId() {
		return id;
	}

	public ProducerOptions setId(String id) {
		this.id = id;
		return this;
	}

    public ProducerListener getProducerListener() {
        return producerListener;
    }

    @SuppressWarnings("unchecked")
    public <T extends ProducerOptions> T setProducerListener(ProducerListener<? extends Topic, ?> producerListener) {
        this.producerListener = producerListener;
        return (T) this;
    }
}
