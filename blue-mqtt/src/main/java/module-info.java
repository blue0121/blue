module blue.mqtt
{
	requires java.xml;
	requires blue.core;
	requires hawtdispatch;
	requires mqtt.client;
	requires hawtbuf;

	exports blue.mqtt;

	exports blue.internal.mqtt.config to spring.beans;
	exports blue.internal.mqtt.consumer to spring.beans;
	exports blue.internal.mqtt.producer to spring.beans;
}