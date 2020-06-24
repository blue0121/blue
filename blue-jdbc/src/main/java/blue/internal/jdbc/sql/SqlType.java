package blue.internal.jdbc.sql;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public enum SqlType
{
	/**
	 * 转义数据库表名和列名
	 */
	ESCAPE,

	/**
	 * 初始化所有默认SQL
	 */
	DEFAULT,

	/**
	 * 动态 insert SQL语句
	 */
	INSERT,

	/**
	 * 动态 update SQL语句
	 */
	UPDATE,

	/**
	 * 动态 delete SQL 语句
	 */
	DELETE,

	/**
	 * 动态增长SQL语句
	 */
	INC,

	/**
	 * 部分 update SQL 语句
	 */
	PARTIAL_UPDATE,

	/**
	 * 部分 insert SQL 语句
	 */
	PARTIAL_INSERT,

	/**
	 * 判断是否存在 SQL 语句
	 */
	EXIST,

	/**
	 * 动态 select ... limit 1 单字段 SQL 语句
	 */
	GET_FIELD,

	/**
	 * 动态 select ... limit SQL语句
	 */
	GET,

	/**
	 * 动态 select count(*) SQL 语句
	 */
	COUNT,

}
