package test.redis.core;

import blue.base.core.message.Topic;
import blue.base.core.util.DateUtil;
import blue.base.core.util.WaitUtil;
import blue.redis.core.RedisCache;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisLock;
import blue.redis.core.RedisProducer;
import blue.redis.core.RedisSequence;
import blue.redis.core.codec.FastjsonCodec;
import blue.redis.core.options.RedisCacheMode;
import blue.redis.core.options.RedisCacheOptions;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
import blue.redis.core.options.RedisSequenceMode;
import blue.redis.core.options.RedisSequenceOptions;
import org.junit.jupiter.api.Assertions;
import test.redis.core.cache.TestLoader;
import test.redis.core.cache.TestWriter;
import test.redis.core.consumer.RedisReceiver;

import java.util.Date;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class RedisMain {
	public RedisMain() {
	}

	public static void main(String[] args) {
		RedisClientOptions clientOptions = new RedisClientOptions();
		clientOptions.setBroker("redis://localhost:6379");
		clientOptions.setCodec(new FastjsonCodec());
		RedisClient client = RedisClient.create(clientOptions);

		RedisConsumerOptions consumerOptions = new RedisConsumerOptions();
		RedisConsumer consumer = client.createConsumer(consumerOptions);
		RedisReceiver receiver = new RedisReceiver();
		consumer.subscribe(new Topic("test"), receiver);

		RedisProducerOptions producerOptions = new RedisProducerOptions();
		RedisProducer producer = client.createProducer(producerOptions);
		int count = 10;
		for (int i = 0; i < count; i++) {
			producer.sendSync(new Topic("test"), "blue_" + i);
		}
		WaitUtil.sleep(1000);
		for (int i = 0; i < count; i++) {
			Assertions.assertTrue(receiver.getMessage().contains("blue_" + i));
		}

		RedisLock lock = client.createLock();
		lock.lock("lock", () -> {
			System.out.println("Redis Lock");
			return null;
		});

		RedisSequenceOptions atomicOptions = new RedisSequenceOptions();
		String atomicPrefix = "A000000";
		atomicOptions.setKey("seq_atomic").setMode(RedisSequenceMode.ATOMIC).setLength(8).setPrefix("A");
		testSequence(client, atomicOptions, atomicPrefix);

		RedisSequenceOptions dateOptions = new RedisSequenceOptions();
		String datePrefix = "D" + DateUtil.format(new Date(), "yyyyMMdd") + "00";
		dateOptions.setKey("seq_date").setMode(RedisSequenceMode.DATE).setLength(12).setPrefix("D");
		testSequence(client, dateOptions, datePrefix);

		TestLoader loader = new TestLoader();
		TestWriter writer = new TestWriter();
		RedisCacheOptions redisOptions = new RedisCacheOptions();
		redisOptions.setPrefix("redis").setMode(RedisCacheMode.REDIS).setLoader(loader).setWriter(writer);
		testCache(client, redisOptions);

		RedisCacheOptions localRedisOptions = new RedisCacheOptions();
		localRedisOptions.setPrefix("local_redis").setMode(RedisCacheMode.LOCAL_REDIS).setLoader(loader).setWriter(writer);
		testCache(client, localRedisOptions);

	    client.disconnect();
    }

	private static void testSequence(RedisClient client, RedisSequenceOptions options, String prefix) {
		int count = 2;
		RedisSequence sequence = client.createSequence(options);
		sequence.reset();
		for (int i = 0; i < count; i++) {
			String val = sequence.nextValue();
			System.out.println(val);
			Assertions.assertEquals(prefix + (i + 1), val);
		}
		sequence.reset();
		for (int i = 0; i < count; i++) {
			String val = sequence.nextValue();
			System.out.println(val);
			Assertions.assertEquals(prefix + (i + 1), val);
		}
    }

    private static void testCache(RedisClient client, RedisCacheOptions options) {
	    RedisCache<String> cache = client.createCache(options);
	    String key = "test";
	    String value = "test_test";

	    String val = cache.get(key);
	    System.out.println(val);
	    Assertions.assertEquals(key, val);

	    cache.set(key, value);
	    val = cache.get(key);
	    System.out.println(val);
	    Assertions.assertEquals(value, val);

	    cache.remove(key);
	    val = cache.get(key);
	    System.out.println(val);
	    Assertions.assertEquals(key, val);

	    cache.set(key, value);
	    val = cache.get(key);
	    System.out.println(val);
	    Assertions.assertEquals(value, val);

	    cache.clear();
	    val = cache.get(key);
	    System.out.println(val);
	    Assertions.assertEquals(key, val);
    }

}
