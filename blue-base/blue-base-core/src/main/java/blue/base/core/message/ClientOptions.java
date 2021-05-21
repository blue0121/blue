package blue.base.core.message;

import blue.base.core.id.IdGenerator;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-29
 */
@SuppressWarnings("rawtypes")
public class ClientOptions {
    private static Logger logger = LoggerFactory.getLogger(ClientOptions.class);

    protected String id;
    protected String broker;

    public ClientOptions() {
    }

    public void check() {
        AssertUtil.notEmpty(broker, "Broker");
        if (id == null || id.isEmpty()) {
            id = "Producer" + IdGenerator.uuid12bit();
        }
    }

    public String getId() {
        return id;
    }

    public ClientOptions setId(String id) {
        this.id = id;
        return this;
    }

    public String getBroker() {
        return broker;
    }

    public ClientOptions setBroker(String broker) {
        this.broker = broker;
        return this;
    }
}
