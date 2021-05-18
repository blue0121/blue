package test.redis.core;

import blue.base.core.message.Topic;
import blue.redis.core.RedisCache;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisLock;
import blue.redis.core.RedisProducer;
import blue.redis.core.Sequence;
import blue.redis.core.codec.FastjsonCodec;
import blue.redis.core.options.RedisCacheMode;
import blue.redis.core.options.RedisCacheOptions;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
import blue.redis.core.options.RedisSequenceMode;
import blue.redis.core.options.RedisSequenceOptions;
import test.redis.core.cache.TestLoader;
import test.redis.core.cache.TestWriter;
import test.redis.core.consumer.RedisReceiver;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class RedisMain {
	public RedisMain() {
	}

    public static void main(String[] args) {
        RedisClientOptions clientOptions = new RedisClientOptions();
        clientOptions.setId("client").setBroker("redis://localhost:6379").setCodec(new FastjsonCodec());
	    RedisClient client = RedisClient.create(clientOptions);

	    RedisConsumerOptions consumerOptions = new RedisConsumerOptions();
	    consumerOptions.setId("consumer");
	    RedisConsumer consumer = client.createConsumer(consumerOptions);
	    RedisReceiver receiver = new RedisReceiver();
	    consumer.subscribe(new Topic("test"), receiver);

	    RedisProducerOptions producerOptions = new RedisProducerOptions();
	    producerOptions.setId("producer");
	    RedisProducer producer = client.createProducer(producerOptions);
	    int count = 10;
	    for (int i = 0; i < count; i++) {
	    	producer.sendSync(new Topic("test"), "blue_" + i);
	    }

	    RedisLock lock = client.createLock();
	    lock.lock("lock", () -> {
	    	System.out.println("Redis Lock");
	    	return null;
	    });

	    RedisSequenceOptions atomicOptions = new RedisSequenceOptions();
	    atomicOptions.setKey("seq_atomic").setMode(RedisSequenceMode.ATOMIC).setLength(8).setPrefix("A");
	    testSequence(client, atomicOptions);

	    RedisSequenceOptions dateOptions = new RedisSequenceOptions();
	    dateOptions.setKey("seq_date").setMode(RedisSequenceMode.DATE).setLength(12).setPrefix("D");
	    testSequence(client, dateOptions);

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

    private static void testSequence(RedisClient client, RedisSequenceOptions options) {
		int count = 2;
	    Sequence sequence = client.createSequence(options);
	    for (int i = 0; i < count; i++) {
		    System.out.println(sequence.nextValue());
	    }
	    sequence.reset();
	    for (int i = 0; i < count; i++) {
		    System.out.println(sequence.nextValue());
	    }
    }

    private static void testCache(RedisClient client, RedisCacheOptions options) {
	    RedisCache<String> cache = client.createCache(options);
	    String key = "test";
	    String value = "test_test";
	    System.out.println(cache.get(key));
	    cache.set(key, value);
	    System.out.println(cache.get(key));
	    cache.remove(key);
	    System.out.println(cache.get(key));
	    cache.set(key, value);
	    System.out.println(cache.get(key));
	    cache.clear();
	    System.out.println(cache.get(key));
    }

}
