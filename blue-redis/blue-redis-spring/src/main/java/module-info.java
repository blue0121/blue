module blue.redis.spring {
	requires java.xml;
	requires blue.base.core;
	requires blue.base.spring;
	requires blue.redis.core;
	requires redisson;
	requires spring.beans;
	requires spring.context;
	requires org.aspectj.weaver;

	exports blue.redis.internal.spring.config to spring.beans;
	exports blue.redis.internal.spring.bean to spring.beans;
}