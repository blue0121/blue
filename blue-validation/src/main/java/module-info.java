module blue.validation
{
	requires blue.core;
	requires fastjson;
	requires transitive java.validation;
	requires transitive com.fasterxml.classmate;

	exports blue.validation;
	exports blue.validation.group;
	exports blue.validation.annotation;

	exports blue.internal.validation.validator;
}