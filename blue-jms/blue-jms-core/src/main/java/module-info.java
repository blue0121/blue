module blue.jms.core {
	requires java.naming;
	requires javax.jms.api;
	requires blue.base.core;
	requires qpid.client;
	requires activemq.client;

	exports blue.jms.core;
	exports blue.jms.core.options;
}