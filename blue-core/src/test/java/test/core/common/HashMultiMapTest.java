package test.core.common;

import blue.core.common.MultiMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-08
 */
public class HashMultiMapTest
{
	public HashMultiMapTest()
	{
	}

	@Test
	public void test1()
	{
		MultiMap<Integer, Integer> map = MultiMap.create();
		map.put(1, 11);
		map.put(1, 12);
		Assertions.assertEquals(1, map.size());
		Set<Integer> set = map.get(1);
		Assertions.assertNotNull(set);
		Assertions.assertEquals(2, set.size());
		Assertions.assertEquals(set, Set.of(11, 12));
		Assertions.assertTrue(map.remove(1));
		Assertions.assertTrue(map.get(1).isEmpty());
		Assertions.assertFalse(map.remove(1));
	}

	@Test
	public void test2()
	{
		MultiMap<Integer, Integer> map = MultiMap.createConcurrent();
		map.put(1, 1);
		map.put(2, 2);
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(1, map.getOne(1));
		Assertions.assertEquals(2, map.getOne(2));
	}

}
