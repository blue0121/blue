package test.base.core.cache;

import blue.base.core.cache.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.base.core.model.Group;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class SingletonTest {
	public SingletonTest() {
	}

	@BeforeEach
	public void beforeEach() {
		Singleton.clear();
	}

	@Test
	public void testGet() {
		Group group1 = Singleton.get(Group.class);
		Assertions.assertEquals(1, Singleton.size());
		Group group2 = Singleton.get(Group.class);
		Assertions.assertEquals(1, Singleton.size());
		Assertions.assertTrue(group1 == group2);
	}

	@Test
	public void testPut() {
		Group group1 = new Group();
		Group group2 = new Group();

		Singleton.put(group1);
		Assertions.assertEquals(1, Singleton.size());
		Assertions.assertThrows(IllegalArgumentException.class, () -> Singleton.put(group2));
		Singleton.put(group1);
		Assertions.assertEquals(1, Singleton.size());
	}

	@Test
	public void testRemove() {
		Group group1 = new Group();
		Singleton.put(group1);
		Assertions.assertEquals(1, Singleton.size());
		Singleton.remove(group1);
		Assertions.assertEquals(0, Singleton.size());
		Singleton.put(group1);
		Assertions.assertEquals(1, Singleton.size());
		Singleton.remove(group1.getClass());
		Assertions.assertEquals(0, Singleton.size());
	}

}
