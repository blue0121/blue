module blue.monitor
{
	requires blue.core;
	requires simpleclient;
	requires simpleclient.pushgateway;

	exports blue.monitor.metrics;

	exports blue.internal.monitor.config to spring.beans;
	exports blue.internal.monitor.metrics to spring.beans;
}