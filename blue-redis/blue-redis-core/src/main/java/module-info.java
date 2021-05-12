module blue.redis.core {
	requires blue.base.core;
	requires fastjson;
	requires io.netty.all;
	requires redisson;
	requires com.github.benmanes.caffeine;

	exports blue.redis.core;
	exports blue.redis.core.options;
}