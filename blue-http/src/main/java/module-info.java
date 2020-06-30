module blue.http
{
	requires java.xml;
	requires blue.core;
	requires blue.validation;
	requires io.netty.all;
	requires fastjson;
	requires freemarker;

	exports blue.http;
	exports blue.http.annotation;
	exports blue.http.exception;
	exports blue.http.filter;
	exports blue.http.message;

	exports blue.internal.http.config to spring.beans;
	exports blue.internal.http.filter to spring.beans;
	exports blue.internal.http.parser to spring.beans;
	exports blue.internal.http.net to spring.beans;

	opens blue.internal.http.message to fastjson;
}