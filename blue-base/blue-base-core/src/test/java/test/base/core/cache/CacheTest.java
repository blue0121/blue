package test.base.core.cache;

import blue.base.core.cache.Cache;
import blue.base.core.cache.CacheBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class CacheTest {
    private Cache<Integer, Integer> cache;

	public CacheTest() {
	}

	@BeforeEach
	public void beforeEach() {
	    cache = CacheBuilder.create().build();
    }

    @Test
    public void test() {
        Assertions.assertNull(cache.get(1));
        cache.put(1, 10);
        Assertions.assertEquals(10, cache.get(1));
        cache.clear();
        Assertions.assertNull(cache.get(1));
    }

}
