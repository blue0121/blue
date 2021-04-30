package blue.base.core.message;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
@SuppressWarnings("rawtypes")
public class ProducerOptions {
    private String id;
    protected ProducerListener producerListener;

	public ProducerOptions() {
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
