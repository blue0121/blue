module blue.core
{
	requires java.net.http;
	requires java.sql;
	requires java.desktop;
	requires transitive java.annotation;
	requires transitive spring.core;
	requires transitive spring.aop;
	requires transitive spring.beans;
	requires transitive spring.context;
	requires transitive org.slf4j;
	requires transitive org.apache.logging.log4j;
	requires snappy.java;
	requires fastjson;
	requires com.github.benmanes.caffeine;

	exports blue.core.common;
	exports blue.core.convert;
	exports blue.core.dict;
	exports blue.core.file;
	exports blue.core.http;
	exports blue.core.id;
	exports blue.core.message;
	exports blue.core.reflect;
	exports blue.core.security;
	exports blue.core.tree;
	exports blue.core.util;

	exports blue.internal.core.config to spring.beans;
	exports blue.internal.core.dict to spring.beans,fastjson;
	exports blue.internal.core.http to spring.beans;
	exports blue.internal.core.message to blue.redis,blue.mqtt,blue.jms,blue.kafka;

	opens blue.core.common to fastjson;

	opens blue.internal.core.dict to fastjson;
}

