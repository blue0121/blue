module blue.kafka.core {
	requires blue.base.core;
	requires kafka.clients;

	exports blue.kafka.core;
	exports blue.kafka.core.codec;
	exports blue.kafka.core.options;

	exports blue.kafka.internal.core.offset to blue.kafka.spring;

}