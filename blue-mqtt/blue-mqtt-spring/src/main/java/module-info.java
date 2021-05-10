module blue.mqtt.spring {
	requires java.xml;
	requires blue.base.core;
	requires blue.base.spring;
	requires blue.mqtt.core;
	requires spring.beans;

	exports blue.mqtt.internal.spring.config to spring.beans;
	exports blue.mqtt.internal.spring.bean to spring.beans;
}