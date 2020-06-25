package blue.jdbc.core;

import blue.internal.jdbc.util.ObjectUtil;
import blue.jdbc.annotation.LockModeType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jdbc object 操作
 *
 * @author Jin Zheng
 * @since 2020-06-25
 */
public interface JdbcOperation
{
	/**
	 * 保存对象
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	default int save(Object object)
	{
		return this.save(object, true);
	}

	/**
	 * 保存对象
	 *
	 * @param object 对象
	 * @param dynamic 空字段是否也写进SQL语句中
	 * @return 影响记录数
	 */
	int save(Object object, boolean dynamic);

	/**
	 * 保存对象
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	default int saveObject(Class<?> clazz, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.saveObject(clazz, map);
	}

	/**
	 * 保存对象
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	int saveObject(Class<?> clazz, Map<String, Object> param);

	/**
	 * 保存对象列表，默认不产生主键
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	int[] saveList(List<?> objectList);

	/**
	 * 更新对象
	 *
	 * @param object
	 * @return 影响记录数
	 */
	default int update(Object object)
	{
		return this.update(object, true);
	}

	/**
	 * 更新对象
	 *
	 * @param object
	 * @param dynamic 空字段是否也写进SQL语句中
	 * @return 影响记录数
	 */
	int update(Object object, boolean dynamic);

	/**
	 * 更新对象
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键值
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	default int updateObject(Class<?> clazz, Object id, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.updateObject(clazz, id, map);
	}

	/**
	 * 更新对象
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键值
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	int updateObject(Class<?> clazz, Object id, Map<String, Object> param);

	/**
	 * 更新对象列表
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	int[] updateList(List<?> objectList);

	/**
	 * 对象自增长
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键
	 * @param params [字段名0, 增长值0, 字段名1, 增长值1...]
	 * @return 影响记录数
	 */
	default int inc(Class<?> clazz, Object id, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.inc(clazz, id, map);
	}

	/**
	 * 对象自增长
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键
	 * @param param <字段名, 增长值>
	 * @return 影响记录数
	 */
	int inc(Class<?> clazz, Object id, Map<String, Object> param);

	/**
	 * 删除对象
	 *
	 * @param object 对象
	 * @return 影响数据库记录数
	 */
	int delete(Object object);

	/**
	 * 根据主键删除对象
	 *
	 * @param id 主键
	 * @return 影响数据库记录数
	 */
	int deleteId(Class<?> clazz, Object id);

	/**
	 * 根据主键列表删除对象
	 *
	 * @param clazz 对象类型
	 * @param idList 主键列表
	 */
	<K, T> int delete(Class<T> clazz, List<K> idList);

	/**
	 * 根据字段值删除
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	default <T> int deleteBy(Class<T> clazz, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.deleteBy(clazz, map);
	}

	/**
	 * 根据字段值删除
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	<T> int deleteBy(Class<T> clazz, Map<String, Object> param);

	/**
	 * 根据主键取得一个对象
	 *
	 * @param id 单主键
	 * @param clazz 对象类型
	 * @return 对象，不存在返回 null
	 */
	default <T> T get(Class<T> clazz, Object id)
	{
		return this.get(clazz, id, LockModeType.NONE);
	}

	/**
	 * 根据主键取得一个对象
	 *
	 * @param id 单主键
	 * @param clazz 对象类型
	 * @param type 锁类型
	 * @return 对象，不存在返回 null
	 */
	<T> T get(Class<T> clazz, Object id, LockModeType type);

	/**
	 * 根据主键取得对象Map
	 *
	 * @param clazz 对象类型
	 * @param idList 主键列表
	 * @return 对象Map
	 */
	<K, T> Map<K, T> get(Class<T> clazz, Collection<K> idList);

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param clazz 类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 单个对象，不存在返回 null
	 */
	default <T> T get(Class<T> clazz, String sql, Map<String, Object> param)
	{
		return this.get(clazz, sql, param, LockModeType.NONE);
	}

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 单个对象，不存在返回 null
	 */
	default <T> T get(String sql, T param)
	{
		return this.get(sql, param, LockModeType.NONE);
	}

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @param type 锁类型
	 * @return 单个对象，不存在返回 null
	 */
	@SuppressWarnings("unchecked")
	default <T> T get(String sql, T param, LockModeType type)
	{
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.get((Class<T>)param.getClass(), sql, map, type);
	}

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param clazz 类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @param type 锁类型
	 * @return 单个对象，不存在返回 null
	 */
	<T> T get(Class<T> clazz, String sql, Map<String, Object> param, LockModeType type);

	/**
	 * 查询一个字段
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段值
	 */
	default <T> T getField(Class<?> clazz, Class<T> target, String field, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.getField(clazz, target, field, map);
	}

	/**
	 * 查询一个字段
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param param <字段名, 字段值>
	 * @return 字段值
	 */
	<T> T getField(Class<?> clazz, Class<T> target, String field, Map<String, Object> param);

	/**
	 * 查询一个对象
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象
	 */
	default <T> T getObject(Class<T> clazz, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.getObject(clazz, map);
	}

	/**
	 * 查询一个对象
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 对象
	 */
	<T> T getObject(Class<T> clazz, Map<String, Object> param);

	/**
	 * 判断对象某些字段是否存在
	 *
	 * @param object 对象
	 * @param names 字段列表
	 * @return true表示存在，false表示不存在
	 */
	boolean exist(Object object, String...names);

	/**
	 * 查询记录数
	 *
	 * @param clazz 类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 记录数
	 */
	default int count(Class<?> clazz, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.count(clazz, map);
	}

	/**
	 * 查询记录数
	 *
	 * @param clazz 类型
	 * @param param <字段名, 字段值>
	 * @return 记录数
	 */
	int count(Class<?> clazz, Map<String, Object> param);

	/**
	 * 根据SQL查询一个整数
	 *
	 * @param <T> 类型参数
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 整数
	 */
	default <T> int queryForInt(String sql, T param)
	{
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.queryForInt(sql, map);
	}

	/**
	 * 根据SQL查询一个整数
	 *
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 整数
	 */
	int queryForInt(String sql, Map<String, Object> param);

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @return 对象列表
	 */
	default <T> List<T> list(Class<T> clazz, String sql)
	{
		return this.list(clazz, sql, new HashMap<>());
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 对象列表
	 */
	default <T, P> List<T> list(Class<T> clazz, String sql, P param)
	{
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.list(clazz, sql, map);
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param start 起始行号
	 * @param size 最大记录数
	 * @return 对象列表
	 */
	default <T> List<T> list(Class<T> clazz, String sql, int start, int size)
	{
		sql = this.getDialect().page(sql, start, size);
		return this.list(clazz, sql, new HashMap<>());
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @param start 起始行号
	 * @param size 最大记录数
	 * @return 对象列表
	 */
	default <T, P> List<T> list(Class<T> clazz, String sql, P param, int start, int size)
	{
		sql = this.getDialect().page(sql, start, size);
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.list(clazz, sql, map);
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @param start 起始行号
	 * @param size 最大记录数
	 * @return 对象列表
	 */
	default  <T> List<T> list(Class<T> clazz, String sql, Map<String, Object> param, int start, int size)
	{
		sql = this.getDialect().page(sql, start, size);
		return this.list(clazz, sql, param);
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 对象列表
	 */
	<T> List<T> list(Class<T> clazz, String sql, Map<String, Object> param);

	/**
	 * 查询字段列表
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段列表
	 */
	default <T> List<T> listField(Class<?> clazz, Class<T> target, String field, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.listField(clazz, target, field, map);
	}

	/**
	 * 查询字段列表
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param param <字段名, 字段值>
	 * @return 字段列表
	 */
	<T> List<T> listField(Class<?> clazz, Class<T> target, String field, Map<String, Object> param);

	/**
	 * 查询对象列表
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象列表
	 */
	default <T> List<T> listObject(Class<T> clazz, Object...params)
	{
		Map<String, Object> map = ObjectUtil.toParamMap(params);
		return this.listObject(clazz, map);
	}

	/**
	 * 查询对象列表
	 *
	 * @param clazz 对象类型
	 * @param param <字段名, 字段值>
	 * @return 对象列表
	 */
	<T> List<T> listObject(Class<T> clazz, Map<String, Object> param);

	/**
	 * 根据类名查找表名
	 *
	 * @param clazz
	 * @return
	 */
	String getTable(Class<?> clazz);

	/**
	 * 获取JdbcTemplate
	 * @return
	 */
	JdbcTemplate getJdbcTemplate();

	/**
	 * 获取NamedParameterJdbcTemplate
	 * @return
	 */
	NamedParameterJdbcTemplate getNamedParameterJdbcTemplate();

	/**
	 * 获取数据库方言
	 * @return
	 */
	Dialect getDialect();

	/**
	 * 是否运行调试模式
	 * @return
	 */
	boolean isDebug();

	/**
	 * 数据库表名或字段名是否转义
	 * @return
	 */
	boolean isEscape();

}
