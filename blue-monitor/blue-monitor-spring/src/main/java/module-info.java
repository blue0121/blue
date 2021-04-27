module blue.monitor.spring {
	requires java.xml;
	requires blue.base.spring;
	requires blue.monitor.core;
	requires spring.beans;
	requires simpleclient;
	requires simpleclient.pushgateway;

	exports blue.monitor.internal.spring.config to spring.beans;
	exports blue.monitor.internal.spring.metrics to spring.beans;
}
