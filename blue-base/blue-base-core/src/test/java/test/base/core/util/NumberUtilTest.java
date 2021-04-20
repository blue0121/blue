package test.base.core.util;

import blue.base.core.util.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NumberUtilTest
{
	public NumberUtilTest()
	{
	}
	
	@Test
	public void testChnToInt()
	{
		Assertions.assertEquals(5, NumberUtil.chnToInt("五"), "转化错误");
		Assertions.assertEquals(15, NumberUtil.chnToInt("十五"), "转化错误");
		Assertions.assertEquals(15, NumberUtil.chnToInt("一十五"), "转化错误");
		Assertions.assertEquals(25, NumberUtil.chnToInt("二十五"), "转化错误");
		Assertions.assertEquals(205, NumberUtil.chnToInt("二百零五"), "转化错误");
	}

	@Test
	public void testIsNumeric()
	{
		Assertions.assertTrue(NumberUtil.isInteger("123"));
		Assertions.assertTrue(NumberUtil.isInteger("-123"));
		Assertions.assertFalse(NumberUtil.isInteger("a123"));
		Assertions.assertFalse(NumberUtil.isInteger("123a"));
		Assertions.assertFalse(NumberUtil.isInteger("aa"));
	}

	@Test
	public void testSplit()
	{
		String str = "5, 10, 50, 100, 150, 200, 500, 1000, 2000, 5000";
		List<Integer> list = NumberUtil.split(str, Integer.class);
		Assertions.assertEquals(10, list.size());
		Assertions.assertTrue(NumberUtil.split("", Integer.class).isEmpty());
	}

	@Test
	public void testSplitInt()
	{
		String str = "5, 10, 50";
		int[] arr = NumberUtil.splitInt(str);
		Assertions.assertEquals(3, arr.length);
		int[] array = {5, 10, 50};
		Assertions.assertArrayEquals(array, arr);
	}
	
}
