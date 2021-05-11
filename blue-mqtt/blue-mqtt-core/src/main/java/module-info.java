module blue.mqtt.core {
	requires blue.base.core;
	requires hawtdispatch;
	requires mqtt.client;
	requires hawtbuf;

	exports blue.mqtt.core;
	exports blue.mqtt.core.options;
}