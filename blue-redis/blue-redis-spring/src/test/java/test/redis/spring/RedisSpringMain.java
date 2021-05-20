package test.redis.spring;

import blue.base.core.message.Topic;
import blue.base.core.util.DateUtil;
import blue.redis.core.RedisProducer;
import blue.redis.core.RedisSequence;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.spring.consumer.RedisReceiver;
import test.redis.spring.model.User;

import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class RedisSpringMain {
	private static Logger logger = LoggerFactory.getLogger(RedisSpringMain.class);

	public RedisSpringMain() {
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/redis.xml");

		printPubSub(ctx);
		printSeq(ctx);

		ctx.close();
	}

	private static void printPubSub(ClassPathXmlApplicationContext ctx) throws Exception {
		RedisProducer producer = ctx.getBean(RedisProducer.class);
		RedisReceiver receiver = ctx.getBean(RedisReceiver.class);
		Topic topic = new Topic("test");
		int count = 10;
		for (int i = 0; i < count; i++) {
			User user = new User(i, "blue_" + i);
			producer.sendSync(topic, user);
		}
		Thread.sleep(1000);
		System.out.println(receiver.getMessage());
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains(new User(i, "blue_" + i)));
		}
	}

	private static void printSeq(ClassPathXmlApplicationContext ctx) throws Exception {
		RedisSequence seq = ctx.getBean("orderSeq", RedisSequence.class);
		String prefix = "OD" + DateUtil.format(new Date(), "yyyyMMdd") + "000";
		seq.reset();
		for (int i = 0; i < 2; i++) {
			String val = seq.nextValue();
			logger.info("orderSeq: {}", val);
			Assertions.assertEquals(prefix + (i + 1), val);
		}
		seq.reset();
		for (int i = 0; i < 2; i++) {
			String val = seq.nextValue();
			logger.info("orderSeq: {}", val);
			Assertions.assertEquals(prefix + (i + 1), val);
		}
	}

}
