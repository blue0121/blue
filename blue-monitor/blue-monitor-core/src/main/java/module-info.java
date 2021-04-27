module blue.monitor.core {
	requires blue.base.core;
	requires simpleclient;
	requires simpleclient.pushgateway;

	exports blue.monitor.core.metrics;
}