package blue.jdbc.core;

import blue.jdbc.annotation.LockModeType;

/**
 * 数据库方言
 * 
 * @author zhengj
 * @since 1.0 2011-4-2
 */
public interface Dialect
{
	/**
	 * 数据库转义字符
	 * 
	 * @return 数据库转义字符
	 */
	String escape();
	
	/**
	 * 把表名或字段加上转义字符
	 * 
	 * @param key 表名或字段
	 * @return 转义后的表名或字段
	 */
	String escape(String key);
	
	/**
	 * 把 SQL 语句加上分页功能
	 * 
	 * @param sql SQL语句
	 * @param start 起始行号
	 * @param size 最大记数数
	 * @return 带分页的 SQL 语句
	 */
	String page(String sql, int start, int size);
	
	/**
	 * 在 SQL 语句上加上锁
	 * 
	 * @param sql SQL语句
	 * @param type 锁类型
	 * @return 带锁的 SQL 语句
	 */
	String lock(String sql, LockModeType type);
}
