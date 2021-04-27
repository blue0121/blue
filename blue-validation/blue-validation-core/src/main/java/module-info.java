module blue.validation.core {
	requires blue.base.core;
	requires fastjson;
	requires transitive jakarta.validation;
	requires com.fasterxml.classmate;

	exports blue.validation.core;
	exports blue.validation.core.group;
	exports blue.validation.core.annotation;
	exports blue.validation.core.validator;
}