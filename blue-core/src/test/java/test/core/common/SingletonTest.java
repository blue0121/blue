package test.core.common;

import blue.core.common.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.core.model.Group;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class SingletonTest
{
	public SingletonTest()
	{
	}

	@Test
	public void test1()
	{
		Group group1 = Singleton.get(Group.class);
		Group group2 = Singleton.get(Group.class);
		Assertions.assertTrue(group1 == group2);
	}

}
