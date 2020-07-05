package test.redis;

import blue.core.message.Topic;
import blue.redis.producer.RedisProducer;
import blue.redis.sequence.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.model.User;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class RedisMain
{
	private static Logger logger = LoggerFactory.getLogger(RedisMain.class);

	public RedisMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/redis.xml");

		printPubSub(ctx);
		printSeq(ctx);

		ctx.close();
	}

	private static void printPubSub(ClassPathXmlApplicationContext ctx) throws Exception
	{
		RedisProducer producer = ctx.getBean(RedisProducer.class);
		Topic topic = new Topic("test");
		for (int i = 0; i < 10; i++)
		{
			User user = new User(i, "blue_" + i);
			producer.sendSync(topic, user);
		}
		Thread.sleep(5000);
	}

	private static void printSeq(ClassPathXmlApplicationContext ctx) throws Exception
	{
		Sequence seq = ctx.getBean("orderSeq", Sequence.class);
		for (int i = 0; i < 2; i++)
		{
			logger.info("orderSeq: {}", seq.nextValue());
		}
		Thread.sleep(31000);
		for (int i = 0; i < 2; i++)
		{
			logger.info("orderSeq: {}", seq.nextValue());
		}
	}

}
