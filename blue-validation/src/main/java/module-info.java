module blue.validation
{
	requires blue.core;
	requires fastjson;
	requires spring.core;
	requires spring.context;
	requires java.validation;

	exports blue.validation;
	exports blue.validation.annotation;
}