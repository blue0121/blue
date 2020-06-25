package blue.jdbc.core;

import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.util.IdUtil;
import blue.jdbc.annotation.LockModeType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-12-01
 */
public abstract class BaseDao<T> extends QueryDao<T, T>
{

	public BaseDao()
	{
	}

	/**
	 * 保存对象，动态生成SQL
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int save(T object)
	{
		return jdbcOperation.save(object);
	}

	/**
	 * 保存对象
	 *
	 * @param object 对象
	 * @param dynamic 是否动态生成SQL
	 * @return 影响记录数
	 */
	public int save(T object, boolean dynamic)
	{
		return jdbcOperation.save(object, dynamic);
	}

	/**
	 * 保存对象
	 *
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public int saveObject(Object...params)
	{
		return jdbcOperation.saveObject(targetClazz, params);
	}

	/**
	 * 保存对象
	 *
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	public int saveObject(Map<String, Object> param)
	{
		return jdbcOperation.saveObject(targetClazz, param);
	}

	/**
	 * 保存对象列表，不动态生成SQL
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	public int[] saveList(List<T> objectList)
	{
		return jdbcOperation.saveList(objectList);
	}

	/**
	 * 删除对象
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int delete(T object)
	{
		return jdbcOperation.delete(object);
	}

	/**
	 * 根据主键删除对象
	 *
	 * @param id 主键
	 * @return 影响数据库记录数
	 */
	public int deleteId(Object id)
	{
		return jdbcOperation.deleteId(targetClazz, id);
	}

	/**
	 * 根据ID列表删除对象
	 *
	 * @param idList ID列表
	 * @return 影响记录数
	 */
	public <K> int delete(List<K> idList)
	{
		return jdbcOperation.delete(targetClazz, idList);
	}

	/**
	 * 根据字段值删除
	 *
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public int deleteBy(Object...params)
	{
		return jdbcOperation.deleteBy(targetClazz, params);
	}

	/**
	 * 更新对象，动态生成SQL
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int update(T object)
	{
		return jdbcOperation.update(object);
	}

	/**
	 * 更新对象
	 *
	 * @param object 对象
	 * @param dynamic 是否动态生成SQL
	 * @return 影响记录数
	 */
	public int update(T object, boolean dynamic)
	{
		return jdbcOperation.update(object, dynamic);
	}

	/**
	 * 更新对象
	 *
	 * @param id 对象主键
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public int updateObject(Object id, Object...params)
	{
		return jdbcOperation.updateObject(targetClazz, id, params);
	}

	/**
	 * 更新对象
	 *
	 * @param id 对象主键
	 * @param param <字段名, 字段值>
	 * @return 影响记录数
	 */
	public int updateObject(Object id, Map<String, Object> param)
	{
		return jdbcOperation.updateObject(targetClazz, id, param);
	}

	/**
	 * 更新对象列表，不动态生成SQL
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	public int[] updateList(List<T> objectList)
	{
		return jdbcOperation.updateList(objectList);
	}

	/**
	 *  对象属性自增长
	 *
	 * @param id 对象主键
	 * @param  params [字段名0(字符串), 增长值0(数字), 字段名1(字符串), 增长值1(数字)...]
	 * @return 影响记录数
	 */
	public int inc(Object id, Object...params)
	{
		return jdbcOperation.inc(targetClazz, id, params);
	}

	/**
	 *  对象属性自增长
	 *
	 * @param id 对象主键
	 * @param  param <字段名(字符串), 增长值(数字)>
	 * @return 影响记录数
	 */
	public int inc(Object id, Map<String, Object> param)
	{
		return jdbcOperation.inc(targetClazz, id, param);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id 主键
	 * @return 对象
	 */
	public T get(Object id)
	{
		return jdbcOperation.get(targetClazz, id);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id 主键
	 * @param type 锁类型
	 * @return 对象
	 */
	public T get(Object id, LockModeType type)
	{
		return jdbcOperation.get(targetClazz, id, type);
	}

	/**
	 * 根据主键取得对象
	 *
	 * @param id 主键
	 * @return 对象
	 */
	public T getSelect(Object id)
	{
		CacheEntity cacheEntity = parserCache.get(targetClazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);

		Map<String, Object> param = new HashMap<>();
		param.put(cacheId.getName(), id);
		StringBuilder sql = this.select();
		sql.append(" where a.").append(cacheId.getEscapeColumn()).append("=:").append(cacheId.getName());
		return jdbcOperation.get(targetClazz, sql.toString(), param);
	}

	/**
	 * 查询一个字段
	 *
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段值
	 */
	public <K> K getField(Class<K> target, String field, Object...params)
	{
		return jdbcOperation.getField(targetClazz, target, field, params);
	}

	/**
	 * 查询一个字段
	 *
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param param <字段名, 字段值>
	 * @return 字段值
	 */
	public <K> K getField(Class<K> target, String field, Map<String, Object> param)
	{
		return jdbcOperation.getField(targetClazz, target, field, param);
	}

	/**
	 * 查询一个对象
	 *
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象
	 */
	public T getObject(Object...params)
	{
		return jdbcOperation.getObject(targetClazz, params);
	}

	/**
	 * 根据主键列表取得对象Map
	 *
	 * @param idList 主键列表
	 * @return 对象Map
	 */
	public <K> Map<K, T> getList(Collection<K> idList)
	{
		return jdbcOperation.get(targetClazz, idList);
	}

	/**
	 * 判断对象某些字段是否存在
	 *
	 * @param object 对象
	 * @param names 字段列表
	 * @return true表示存在，false表示不存在
	 */
	public boolean exist(T object, String...names)
	{
		return jdbcOperation.exist(object, names);
	}

	/**
	 * 查询字段列表
	 *
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段列表
	 */
	public <K> List<K> listField(Class<K> target, String field, Object...params)
	{
		return jdbcOperation.listField(targetClazz, target, field, params);
	}

	/**
	 * 查询字段列表
	 *
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param param <字段名, 字段值>
	 * @return 字段列表
	 */
	public <K> List<K> listField(Class<K> target, String field, Map<String, Object> param)
	{
		return jdbcOperation.listField(targetClazz, target, field, param);
	}

	/**
	 * 查询对象列表
	 *
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象列表
	 */
	public List<T> listObject(Object...params)
	{
		return jdbcOperation.listObject(targetClazz, params);
	}


	/**
	 * 查询对象列表
	 *
	 * @param param <字段名, 字段值>
	 * @return 对象列表
	 */
	public List<T> listObject(Map<String, Object> param)
	{
		return jdbcOperation.listObject(targetClazz, param);
	}

	/**
	 * 查询记录数
	 *
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 记录数
	 */
	public int count(Object...params)
	{
		return jdbcOperation.count(targetClazz, params);
	}

	/**
	 * 查询记录数
	 *
	 * @param param <字段名, 字段值>
	 * @return 记录数
	 */
	public int count(Map<String, Object> param)
	{
		return jdbcOperation.count(targetClazz, param);
	}

}
