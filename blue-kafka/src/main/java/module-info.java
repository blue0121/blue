module blue.kafka
{
	requires java.xml;
	requires blue.core;
	requires kafka.clients;

	exports blue.kafka;

	//exports blue.internal.kafka.codec to kafka.clients;
	exports blue.internal.kafka.config to spring.beans;
	exports blue.internal.kafka.consumer to spring.beans;
	exports blue.internal.kafka.offset to spring.beans;
	exports blue.internal.kafka.producer to spring.beans;
}