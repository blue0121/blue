module blue.base.spring {
	requires blue.base.core;
	requires java.sql;
	requires spring.core;
	requires spring.aop;
	requires spring.beans;
	requires spring.context;

	exports blue.base.spring.common;

	exports blue.base.internal.spring.config to spring.beans;
	exports blue.base.internal.spring.dict to spring.beans;
}