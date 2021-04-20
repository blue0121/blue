module blue.base.core {
	requires java.net.http;
	requires java.sql;
	requires java.desktop;
	requires transitive org.slf4j;
	requires transitive org.apache.logging.log4j;
	requires snappy.java;
	requires fastjson;
	requires com.github.benmanes.caffeine;

	exports blue.base.core.collection;
	exports blue.base.core.common;
	exports blue.base.core.dict;
	exports blue.base.core.file;
	exports blue.base.core.id;
	exports blue.base.core.reflect;
	exports blue.base.core.util;

	exports blue.base.internal.core.dict to fastjson;
}