module blue.http.ext
{
	requires java.xml;
	requires blue.core;
	requires blue.http;
	requires blue.monitor.core;
	requires simpleclient;
	requires simpleclient.common;
	requires simpleclient.hotspot;

	exports blue.internal.http.ext.config to spring.beans;
	exports blue.internal.http.ext.management to spring.beans;
}