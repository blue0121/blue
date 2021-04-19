package test.base.core.collection;

import blue.base.internal.core.collection.ConcurrentHashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-08
 */
public class ConcurrentHashSetTest {
	public ConcurrentHashSetTest() {
	}

	@Test
	public void test1() {
		Set<Integer> set = new ConcurrentHashSet<>();
		set.add(1);
		set.add(2);
		Assertions.assertEquals(2, set.size());
		set.add(2);
		Assertions.assertEquals(2, set.size());
	}

}
