module blue.http.extension
{
	requires blue.core;
	requires blue.http;
	requires blue.monitor;
	requires simpleclient;
	requires simpleclient.common;
	requires simpleclient.hotspot;

	exports blue.internal.http.extension.config to spring.beans;
	exports blue.internal.http.extension.monitor to spring.beans;
}