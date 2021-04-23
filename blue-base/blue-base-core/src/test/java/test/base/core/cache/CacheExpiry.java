package test.base.core.cache;

import blue.base.core.cache.Cache;
import blue.base.core.cache.CacheBuilder;
import blue.base.core.cache.Expiry;
import blue.base.core.util.WaitUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-23
 */
public class CacheExpiry {
    private Cache<Integer, Integer> cache;

	public CacheExpiry() {
	}

	@BeforeEach
	public void beforeEach() {
	    this.cache = CacheBuilder.create().expire(new Expiry<Integer, Integer>() {

            @Override
            public long expireAfterCreate(Integer key, Integer value, long currentTimeMillis) {
                return 20;
            }

            @Override
            public long expireAfterUpdate(Integer key, Integer value, long currentTimeMillis, long currentDurationMillis) {
                return 20;
            }

            @Override
            public long expireAfterRead(Integer key, Integer value, long currentTimeMillis, long currentDurationMillis) {
                return currentDurationMillis;
            }
        }).build();
    }

    @Test
    public void testExpiry() {
	    cache.put(1, 1);
        Assertions.assertEquals(1, cache.get(1));
        WaitUtil.sleep(50);
        Assertions.assertNull(cache.get(1));
        cache.put(1, 10);
        Assertions.assertEquals(10, cache.get(1));
        WaitUtil.sleep(10);
        Assertions.assertEquals(10, cache.get(1));
        WaitUtil.sleep(20);
        Assertions.assertNull(cache.get(1));
    }

}
