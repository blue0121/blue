module blue.kafka.spring {
	requires java.xml;
	requires blue.base.core;
	requires blue.base.spring;
	requires blue.kafka.core;
	requires spring.beans;

	exports blue.kafka.internal.spring.config to spring.beans;
	exports blue.kafka.internal.spring.bean to spring.beans;
}