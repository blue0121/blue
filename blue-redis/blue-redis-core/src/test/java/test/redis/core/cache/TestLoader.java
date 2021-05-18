package test.redis.core.cache;

import blue.redis.core.RedisCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class TestLoader implements RedisCacheLoader<String> {
    private static Logger logger = LoggerFactory.getLogger(TestLoader.class);

	public TestLoader() {
	}

    @Override
    public String load(String key) {
	    logger.info("Load key: {}", key);
        return key;
    }

    @Override
    public Iterable<String> loadAllKeys() {
	    logger.info("Load all keys");
        return List.of("test");
    }
}
