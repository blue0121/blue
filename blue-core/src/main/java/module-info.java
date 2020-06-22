module blue.core
{
	requires java.net.http;
	requires java.sql;
	requires java.desktop;
	requires transitive java.annotation;
	requires transitive spring.core;
	requires transitive spring.beans;
	requires transitive spring.context;
	requires transitive org.slf4j;
	requires transitive org.apache.logging.log4j;
	requires snappy.java;
	requires fastjson;

	exports blue.core.common;
	exports blue.core.convert;
	exports blue.core.dict;
	exports blue.core.file;
	exports blue.core.http;
	exports blue.core.id;
	exports blue.core.security;
	exports blue.core.tree;
	exports blue.core.util;

	exports blue.internal.core.config to spring.beans;
	exports blue.internal.core.dict to spring.beans;
}