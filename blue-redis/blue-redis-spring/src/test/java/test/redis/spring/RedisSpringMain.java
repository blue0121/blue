package test.redis.spring;

import blue.base.core.message.Topic;
import blue.redis.core.RedisProducer;
import blue.redis.core.RedisSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.spring.model.User;

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
		Topic topic = new Topic("test");
		for (int i = 0; i < 10; i++) {
			User user = new User(i, "blue_" + i);
			producer.sendSync(topic, user);
		}
		Thread.sleep(1000);
	}

	private static void printSeq(ClassPathXmlApplicationContext ctx) throws Exception {
		RedisSequence seq = ctx.getBean("orderSeq", RedisSequence.class);
		for (int i = 0; i < 2; i++) {
			logger.info("orderSeq: {}", seq.nextValue());
		}
		seq.reset();
		for (int i = 0; i < 2; i++) {
			logger.info("orderSeq: {}", seq.nextValue());
		}
	}

}
