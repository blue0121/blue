package test.kafka.offset;

import blue.internal.kafka.offset.MemoryOffsetManager;
import blue.internal.kafka.offset.OffsetManager;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-03-11
 */
public class MemoryOffsetManagerTest
{
	private OffsetManager offsetManager;
	private TopicPartition partition1;
	private TopicPartition partition2;

	public MemoryOffsetManagerTest()
	{
	}

	@BeforeEach
	public void before()
	{
		offsetManager = new MemoryOffsetManager();
		partition1 = new TopicPartition("test", 0);
		partition2 = new TopicPartition("test", 1);
	}

	@Test
	public void testSingThread()
	{
		offsetManager.commitSingleThread(partition1, 1);
		offsetManager.commitSingleThread(partition1, 2);
		Map<TopicPartition, Long> map = offsetManager.getOffsetMap(Collections.singleton(partition1));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(3, map.get(partition1).longValue());
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 0L));
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 1L));
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 2L));
		Assertions.assertFalse(offsetManager.isCommitted(partition1, 3L));
	}

	@Test
	public void testMultiThread()
	{
		offsetManager.commitMultiThread(partition1, 1);
		offsetManager.commitMultiThread(partition1, 2);
		offsetManager.commitMultiThread(partition1, 4);
		offsetManager.commitMultiThread(partition1, 5);
		offsetManager.mergeMultiThread(partition1);
		Map<TopicPartition, Long> map = offsetManager.getOffsetMap(Collections.singleton(partition1));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(3, map.get(partition1).longValue());
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 1L));
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 2L));
		Assertions.assertFalse(offsetManager.isCommitted(partition1, 3L));
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 4L));
		Assertions.assertTrue(offsetManager.isCommitted(partition1, 5L));
	}

}
