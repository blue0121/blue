module blue.jdbc
{
	requires transitive java.sql;
	requires blue.core;
	requires transitive spring.jdbc;
	requires org.aspectj.weaver;
	requires commons.dbcp2;

	exports blue.jdbc.annotation;
	exports blue.jdbc.core;
	exports blue.jdbc.exception;

	exports blue.internal.jdbc.config to spring.beans;
	exports blue.internal.jdbc.core to spring.beans;

	opens blue.internal.jdbc.datasource to spring.beans;
}