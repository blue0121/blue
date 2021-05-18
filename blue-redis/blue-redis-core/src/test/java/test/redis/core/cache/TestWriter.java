package test.redis.core.cache;

import blue.redis.core.RedisCacheWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-18
 */
public class TestWriter implements RedisCacheWriter<String> {
    private static Logger logger = LoggerFactory.getLogger(TestWriter.class);

	public TestWriter() {
	}

    @Override
    public void delete(Collection<String> keys) {
        logger.info("Delete keys: {}", keys);
    }

    @Override
    public void write(Map<String, String> map) {
        logger.info("Write map: {}", map);
    }
}
