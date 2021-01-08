module blue.redis
{
	requires java.xml;
	requires blue.core;
	requires fastjson;
	requires io.netty.all;
	requires redisson;
	requires com.github.benmanes.caffeine;

	exports blue.redis;

	exports blue.internal.redis.cache to spring.beans;
	exports blue.internal.redis.codec to spring.beans,redisson;
	exports blue.internal.redis.config to spring.beans;
	exports blue.internal.redis.consumer to spring.beans;
	exports blue.internal.redis.lock to spring.beans;
	exports blue.internal.redis.producer to spring.beans;
	exports blue.internal.redis.sequence to spring.beans;

	//opens blue.internal.redis.codec to redisson;
}