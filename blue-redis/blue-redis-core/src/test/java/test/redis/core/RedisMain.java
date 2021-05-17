package test.redis.core;

import blue.base.core.message.Topic;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisLock;
import blue.redis.core.RedisProducer;
import blue.redis.core.Sequence;
import blue.redis.core.codec.FastjsonCodec;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
import blue.redis.core.options.RedisSequenceMode;
import blue.redis.core.options.RedisSequenceOptions;
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

}
