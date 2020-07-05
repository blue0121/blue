package test.mqtt.consumer;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-21
 */
public class MqttTopicUtilTest
{
	public MqttTopicUtilTest()
	{
	}

	@Test
	public void testTopic1()
	{
		String topic = "$share/group/test/+";
		int index = topic.indexOf('/', "$share/".length());
		String subTopic = topic.substring(index + 1);
		Assertions.assertEquals("test/+", subTopic);
	}

	@Test
	public void testTopic2()
	{
		String topic = "$queue/test/+";
		int index = topic.indexOf('/');
		String subTopic = topic.substring(index + 1);
		Assertions.assertEquals("test/+", subTopic);
	}

}
