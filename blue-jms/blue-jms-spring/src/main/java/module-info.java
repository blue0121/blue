module blue.jms.spring {
	requires java.xml;
	requires blue.base.core;
	requires blue.base.spring;
	requires blue.jms.core;
	requires spring.beans;

	exports blue.jms.internal.spring.config to spring.beans;
	exports blue.jms.internal.spring.bean to spring.beans;
}