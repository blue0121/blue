module blue.redis
{
	requires java.xml;
	requires blue.core;
	requires fastjson;
	requires io.netty.all;
	requires redisson;

	exports blue.redis.exception;
	exports blue.redis.lock;
	exports blue.redis.producer;
	exports blue.redis.sequence;

	exports blue.internal.redis.codec to spring.beans;
	exports blue.internal.redis.config to spring.beans;
	exports blue.internal.redis.consumer to spring.beans;
	exports blue.internal.redis.lock to spring.beans;
	exports blue.internal.redis.producer to spring.beans;
	exports blue.internal.redis.sequence to spring.beans;
}