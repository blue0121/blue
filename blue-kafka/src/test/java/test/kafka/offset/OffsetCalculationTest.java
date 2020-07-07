package test.kafka.offset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author Jin Zheng
 * @since 1.0 2019-03-04
 */
public class OffsetCalculationTest
{
	public OffsetCalculationTest()
	{
	}

	@Test
	public void test1()
	{
		int[] offsets = {1,2,3,4,5,6,7,8,9,10};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(10, offset);
	}

	@Test
	public void test2()
	{
		int[] offsets = {1,2,3,4,5,6,7,8,9,10,12};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(10, offset);
	}

	@Test
	public void test3()
	{
		int[] offsets = {1,2,3,4,7,8,9,10,11,12,13,14,17};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(4, offset);
	}

	@Test
	public void test4()
	{
		int[] offsets = {1,3,5,6,8,9,10};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(1, offset);
	}

	@Test
	public void test5()
	{
		int[] offsets = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,40};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(38, offset);
	}

	@Test
	public void test6()
	{
		int[] offsets = {22, 23, 24, 25, 26, 27, 28, 29, 30};
		int offset = this.getOffset(offsets);
		Assertions.assertEquals(30, offset);
	}

	@Test
	public void testSortedSet1()
	{
		Integer[] offsets = {1,2,3,4,5,6,7,8,9,10};
		int[] offsetss = {1,2,3,4,5,6,7,8,9,10};
		NavigableSet<Integer> set = new TreeSet<Integer>(Arrays.asList(offsets));
		int offset = this.getOffset(offsetss);
		NavigableSet<Integer> newSet = set.tailSet(offset, false);
		Assertions.assertEquals(0, newSet.size());
	}

	@Test
	public void testSortedSet2()
	{
		Integer[] offsets = {1,2,3,4,5,6,7,8,9,10,12};
		int[] offsetss = {1,2,3,4,5,6,7,8,9,10,12};
		NavigableSet<Integer> set = new TreeSet<Integer>(Arrays.asList(offsets));
		int offset = this.getOffset(offsetss);
		NavigableSet<Integer> newSet = set.tailSet(offset, false);
		NavigableSet<Integer> expectSet = new TreeSet<>(Arrays.asList(12));
		Assertions.assertEquals(expectSet, newSet);
	}

	@Test
	public void testSortedSet3()
	{
		Integer[] offsets = {1,2,3,4,7,8,9,10,11,12,13,14,17};
		int[] offsetss = {1,2,3,4,7,8,9,10,11,12,13,14,17};
		NavigableSet<Integer> set = new TreeSet<Integer>(Arrays.asList(offsets));
		int offset = this.getOffset(offsetss);
		NavigableSet<Integer> newSet = set.tailSet(offset, false);
		NavigableSet<Integer> expectSet = new TreeSet<>(Arrays.asList(7,8,9,10,11,12,13,14,17));
		Assertions.assertEquals(expectSet, newSet);
	}

	@Test
	public void testSortedSet4()
	{
		Integer[] offsets = {1,3,5,6,8,9,10};
		int[] offsetss = {1,3,5,6,8,9,10};
		NavigableSet<Integer> set = new TreeSet<Integer>(Arrays.asList(offsets));
		int offset = this.getOffset(offsetss);
		NavigableSet<Integer> newSet = set.tailSet(offset, false);
		NavigableSet<Integer> expectSet = new TreeSet<>(Arrays.asList(3,5,6,8,9,10));
		Assertions.assertEquals(expectSet, newSet);
	}

	@Test
	public void testSortedSet5()
	{
		Integer[] offsets = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,40};
		int[] offsetss = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,40};
		NavigableSet<Integer> set = new TreeSet<Integer>(Arrays.asList(offsets));
		int offset = this.getOffset(offsetss);
		NavigableSet<Integer> newSet = set.tailSet(offset, false);
		NavigableSet<Integer> expectSet = new TreeSet<>(Arrays.asList(40));
		Assertions.assertEquals(expectSet, newSet);
	}

	private int getOffset(int[] offsets)
	{
		int len = offsets.length;
		int min = offsets[0];
		int max = offsets[len-1];
		int offset = 0;
		if (len - 1 == max-min)
		{
			offset = max;
		}
		else
		{
			offset = this.calculate(offsets);
		}
		return offset;
	}

	private int calculate(int[] offsets)
	{
		int i = 0;
		int offset = 0;
		int minIndex = 0;
		int maxIndex = offsets.length - 1;
		int middleIndex = (maxIndex + minIndex) / 2;
		while (minIndex + 1 < maxIndex && i < 10)
		{
			if (middleIndex - minIndex == offsets[middleIndex] - offsets[minIndex])
			{
				minIndex = middleIndex;
			}
			else
			{
				maxIndex = middleIndex;
			}
			middleIndex = (maxIndex + minIndex) / 2;
			offset = offsets[middleIndex];
			System.out.printf("minIndex: %d, maxIndex: %d, middleIndex: %d, offset: %d\n", minIndex, maxIndex, middleIndex, offset);
			i++;
		}

		return offset;
	}

}
