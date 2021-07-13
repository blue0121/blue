package test.base.core.cache;

import blue.base.core.cache.Cache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class CacheLoaderTest {
    private Cache<Integer, Integer> cache;

    public CacheLoaderTest() {
    }

    @BeforeEach
    public void beforeEach() {
        cache = Cache.builder().build(k -> 10 * k);
    }

    @Test
    public void testGet() {
        Assertions.assertEquals(10, cache.get(1));
        Assertions.assertEquals(100, cache.get(10));
        cache.put(10, 10);
        Assertions.assertEquals(10, cache.get(10));
    }

}
