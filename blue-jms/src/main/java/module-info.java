module blue.jms
{
	requires java.xml;
	requires javax.jms.api;
	requires blue.core;
	requires qpid.client;

	exports blue.jms;

	exports blue.internal.jms.config to spring.beans;
	exports blue.internal.jms.consumer to spring.beans;
	exports blue.internal.jms.producer to spring.beans;
}