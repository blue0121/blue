module blue.core
{
	requires java.net.http;
	requires transitive java.xml;
	requires transitive java.annotation;
	requires transitive spring.core;
	requires transitive spring.beans;
	requires transitive spring.context;
	requires transitive org.slf4j;
	requires transitive org.apache.logging.log4j;
	requires fastjson;

	exports blue.core.common;
	exports blue.core.dict;
	exports blue.core.file;
	exports blue.core.id;
	exports blue.core.util;


}