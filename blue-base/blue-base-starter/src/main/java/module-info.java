module blue.base.starter {
	requires blue.base.core;
	requires spring.core;
	requires spring.beans;
	requires spring.context;
	requires spring.boot;
	requires spring.boot.autoconfigure;

	exports blue.base.internal.starter.config to spring.beans,spring.context;
	exports blue.base.internal.starter.property to spring.beans,spring.boot;

	opens blue.base.internal.starter.config to spring.core;
}