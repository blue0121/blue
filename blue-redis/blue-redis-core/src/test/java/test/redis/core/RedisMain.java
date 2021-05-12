package test.redis.core;

import blue.base.core.message.Topic;
import blue.redis.core.RedisClient;
import blue.redis.core.RedisConsumer;
import blue.redis.core.RedisProducer;
import blue.redis.core.options.RedisClientOptions;
import blue.redis.core.options.RedisConsumerOptions;
import blue.redis.core.options.RedisProducerOptions;
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
        clientOptions.setId("client").setBroker("redis://localhost:6379");
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

	    client.disconnect();
    }

}
