package blue.jdbc.core;

import blue.core.file.ClassScanner;
import blue.core.id.MachineIdProvider;
import blue.core.id.SnowflakeId;
import blue.core.util.AssertUtil;
import blue.core.util.JsonUtil;
import blue.core.util.StringUtil;
import blue.internal.jdbc.dialect.DetectDialect;
import blue.internal.jdbc.metadata.CheckTable;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.EntityClassHandler;
import blue.internal.jdbc.parser.ParserCache;
import blue.internal.jdbc.sql.SqlHandlerFactory;
import blue.internal.jdbc.sql.SqlType;
import blue.internal.jdbc.util.IdUtil;
import blue.internal.jdbc.util.ObjectRowMapper;
import blue.internal.jdbc.util.ObjectUtil;
import blue.jdbc.annotation.LockModeType;
import blue.jdbc.exception.JdbcException;
import blue.jdbc.exception.VersionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc 核心类
 *
 * @author Jin Zheng
 * @since 2019-11-30
 */
public class JdbcObjectTemplate implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(JdbcObjectTemplate.class);

	private List<String> packageList;
	private DataSource dataSource;
	private Dialect dialect;

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate nJdbcTemplate;
	private ParserCache parserCache;
	private SqlHandlerFactory factory;
	private boolean debug = false;
	private boolean escape = true;
	private SnowflakeId snowflakeId;

	public JdbcObjectTemplate()
	{
		this.parserCache = ParserCache.getInstance();
	}

	/**
	 * 保存对象
	 *
	 * @param object 对象
	 * @return 影响记录数
	 */
	public int save(Object object)
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
	public int save(Object object, boolean dynamic)
	{
		AssertUtil.notNull(object, "Object");
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		Map<String, Object> map = ObjectUtil.toMap(object);
		Map<String, Object> idMap = IdUtil.generateId(object.getClass(), snowflakeId);
		map.putAll(idMap);

		String sql = null;
		if (dynamic)
		{
			sql = factory.handleMap(SqlType.INSERT, object.getClass(), map);
		}
		else
		{
			sql = cacheEntity.getInsertSQL();
		}
		this.log(sql, map);

		KeyHolder key = new GeneratedKeyHolder();
		int n = nJdbcTemplate.update(sql, new MapSqlParameterSource(map), key);
		IdUtil.setId(key, object);
		return n;
	}

	/**
	 * 保存对象
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public int saveObject(Class<?> clazz, Object...params)
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
	public int saveObject(Class<?> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.PARTIAL_INSERT, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.update(sql, param);
	}

	/**
	 * 保存对象列表，默认不产生主键
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	@SuppressWarnings("unchecked")
	public int[] saveList(List<?> objectList)
	{
		if (objectList == null || objectList.size() == 0)
			return new int[0];

		Object object = objectList.get(0);
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		String sql = cacheEntity.getInsertSQL();

		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Object obj : objectList)
		{
			Map<String, Object> map = ObjectUtil.toMap(obj);
			Map<String, Object> idMap = IdUtil.generateId(obj.getClass());
			map.putAll(idMap);
			mapList.add(map);
		}
		this.log(sql, null);

		return nJdbcTemplate.batchUpdate(sql, mapList.toArray(new Map[0]));
	}

	/**
	 * 更新对象
	 *
	 * @param object
	 * @return 影响记录数
	 */
	public int update(Object object)
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
	public int update(Object object, boolean dynamic)
	{
		AssertUtil.notNull(object, "Object");
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		Map<String, Object> map = ObjectUtil.toMap(object);

		String sql = null;
		CacheVersion cacheVersion = cacheEntity.getVersion();
		if (dynamic)
		{
			sql = factory.handleMap(SqlType.UPDATE, object.getClass(), map);
		}
		else
		{
			if (cacheVersion != null)
			{
				sql = cacheEntity.getUpdateVersionSQL();
			}
			else
			{
				sql = cacheEntity.getUpdateSQL();
			}
		}
		this.log(sql, map);

		int n = nJdbcTemplate.update(sql, map);
		if (cacheVersion != null && n == 0)
			throw new VersionException(object.getClass());

		return n;
	}

	/**
	 * 更新对象
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键值
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public int updateObject(Class<?> clazz, Object id, Object...params)
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
	public int updateObject(Class<?> clazz, Object id, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(id, "ID");

		CacheEntity cacheEntity = parserCache.get(clazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);
		String sql = factory.handleMap(SqlType.PARTIAL_UPDATE, clazz, param);
		this.log(sql, param);
		param.put(cacheId.getName(), id);
		return nJdbcTemplate.update(sql, param);
	}

	/**
	 * 更新对象列表
	 *
	 * @param objectList 对象列表
	 * @return 影响记录数
	 */
	@SuppressWarnings("unchecked")
	public int[] updateList(List<?> objectList)
	{
		if (objectList == null || objectList.size() == 0)
			return new int[0];

		Object object = objectList.get(0);
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		String sql = cacheEntity.getUpdateSQL();
		CacheVersion cacheVersion = cacheEntity.getVersion();
		if (cacheVersion != null)
		{
			sql = cacheEntity.getUpdateVersionSQL();
		}

		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Object obj : objectList)
		{
			Map<String, Object> map = ObjectUtil.toMap(obj);
			mapList.add(map);
		}
		this.log(sql, null);

		int[] ns = nJdbcTemplate.batchUpdate(sql, mapList.toArray(new Map[0]));
		for (int n : ns)
		{
			if (cacheEntity.getVersion() != null && n == 0)
				throw new VersionException(object.getClass());
		}

		return ns;
	}

	/**
	 * 对象自增长
	 *
	 * @param clazz 对象类型
	 * @param id 对象主键
	 * @param params [字段名0, 增长值0, 字段名1, 增长值1...]
	 * @return 影响记录数
	 */
	public int inc(Class<?> clazz, Object id, Object...params)
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
	public int inc(Class<?> clazz, Object id, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(id, "ID");

		CacheEntity cacheEntity = parserCache.get(clazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);

		String sql = factory.handleMap(SqlType.INC, clazz, param);
		this.log(sql, param);
		param.put(cacheId.getName(), id);
		return nJdbcTemplate.update(sql, param);
	}

	/**
	 * 删除对象
	 *
	 * @param object 对象
	 * @return 影响数据库记录数
	 */
	public int delete(Object object)
	{
		AssertUtil.notNull(object, "Object");
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		String sql = cacheEntity.getDeleteSQL();
		Map<String, Object> source = IdUtil.getId(object);
		this.log(sql, source);

		return nJdbcTemplate.update(sql, source);
	}

	/**
	 * 根据主键删除对象
	 *
	 * @param id 主键
	 * @return 影响数据库记录数
	 */
	public int deleteId(Class<?> clazz, Object id)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(id, "ID");
		CacheEntity cacheEntity = parserCache.get(clazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);

		String sql = cacheEntity.getDeleteSQL();
		Map<String, Object> param = new HashMap<>();
		param.put(cacheId.getName(), id);
		this.log(sql, param);

		return nJdbcTemplate.update(sql, param);
	}

	/**
	 * 根据主键列表删除对象
	 *
	 * @param clazz 对象类型
	 * @param idList 主键列表
	 */
	public <K, T> int delete(Class<T> clazz, List<K> idList)
	{
		if (idList == null || idList.isEmpty())
			return 0;

		AssertUtil.notNull(clazz, "Class");
		CacheEntity cacheEntity = parserCache.get(clazz);
		IdUtil.checkSingleId(cacheEntity);

		String sql = String.format(cacheEntity.getDeleteListSQL(), StringUtil.join(idList, ","));
		this.log(sql, null);
		return jdbcTemplate.update(sql);
	}

	/**
	 * 根据字段值删除
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 影响记录数
	 */
	public <T> int deleteBy(Class<T> clazz, Object...params)
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
	public <T> int deleteBy(Class<T> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.DELETE, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.update(sql, param);
	}

	/**
	 * 根据主键取得一个对象
	 *
	 * @param id 单主键
	 * @param clazz 对象类型
	 * @return 对象，不存在返回 null
	 */
	public <T> T get(Class<T> clazz, Object id)
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
	public <T> T get(Class<T> clazz, Object id, LockModeType type)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(id, "ID");
		if (type == null)
		{
			type = LockModeType.NONE;
		}

		CacheEntity cacheEntity = parserCache.get(clazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);

		String sql = dialect.lock(dialect.page(cacheEntity.getGetSQL(), 0, 1), type);
		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);

		Map<String, Object> map = new HashMap<>();
		map.put(cacheId.getName(), id);

		this.log(sql, map);

		List<T> list = nJdbcTemplate.query(sql, map, mapper);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);
	}

	/**
	 * 根据主键取得对象Map
	 *
	 * @param clazz 对象类型
	 * @param idList 主键列表
	 * @return 对象Map
	 */
	@SuppressWarnings("unchecked")
	public <K, T> Map<K, T> get(Class<T> clazz, Collection<K> idList)
	{
		AssertUtil.notNull(clazz, "Class");
		CacheEntity cacheEntity = parserCache.get(clazz);
		CacheId cacheId = IdUtil.checkSingleId(cacheEntity);

		Map<K, T> map = new HashMap<>();
		if (idList == null || idList.isEmpty())
			return map;

		String sql = String.format(cacheEntity.getGetListSQL(), StringUtil.join(idList, ","));
		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		this.log(sql, null);

		List<T> list = nJdbcTemplate.query(sql, mapper);
		if (list == null || list.isEmpty())
			return map;

		for (T obj : list)
		{
			try
			{
				K key = (K)cacheId.getGetterMethod().invoke(obj);
				map.put(key, obj);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param clazz 类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 单个对象，不存在返回 null
	 */
	public <T> T get(Class<T> clazz, String sql, Map<String, Object> param)
	{
		return this.get(clazz, sql, param, LockModeType.NONE);
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
	public <T> T get(Class<T> clazz, String sql, Map<String, Object> param, LockModeType type)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notEmpty(sql, "SQL");
		if (type == null)
		{
			type = LockModeType.NONE;
		}

		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		sql = dialect.lock(dialect.page(sql, 0, 1), type);
		this.log(sql, param);

		List<T> list = nJdbcTemplate.query(sql, param, mapper);
		if (list.size() == 0)
			return null;

		return list.get(0);
	}

	/**
	 * 根据SQL查询一个对象
	 *
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 单个对象，不存在返回 null
	 */
	public <T> T get(String sql, T param)
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
	public <T> T get(String sql, T param, LockModeType type)
	{
		AssertUtil.notNull(param, "Param");
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.get((Class<T>)param.getClass(), sql, map, type);
	}

	/**
	 * 查询一个字段
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段值
	 */
	public <T> T getField(Class<?> clazz, Class<T> target, String field, Object...params)
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
	public <T> T getField(Class<?> clazz, Class<T> target, String field, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(target, "Field class");
		AssertUtil.notEmpty(field, "Field");

		String sql = factory.handleField(SqlType.GET_FIELD, clazz, field, param);
		sql = dialect.page(sql, 0, 1);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.queryForList(sql, param, target);
		if (list.isEmpty())
			return null;

		return list.get(0);
	}

	/**
	 * 查询一个对象
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象
	 */
	public <T> T getObject(Class<T> clazz, Object...params)
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
	public <T> T getObject(Class<T> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");

		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		String sql = factory.handleMap(SqlType.GET, clazz, param);
		sql = dialect.page(sql, 0, 1);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.query(sql, param, mapper);
		if (list.isEmpty())
			return null;

		return list.get(0);
	}

	/**
	 * 判断对象某些字段是否存在
	 *
	 * @param object 对象
	 * @param names 字段列表
	 * @return true表示存在，false表示不存在
	 */
	@SuppressWarnings("varargs")
	public boolean exist(Object object, String...names)
	{
		AssertUtil.notNull(object, "Object");
		Class<?> clazz = object.getClass();
		Map<String, Object> map = ObjectUtil.toMap(object);
		String sql = factory.handleMap(SqlType.EXIST, clazz, map, names);
		this.log(sql, map);
		int n = nJdbcTemplate.queryForObject(sql, map, Integer.class);
		return n > 0;
	}

	/**
	 * 查询记录数
	 *
	 * @param clazz 类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 记录数
	 */
	public int count(Class<?> clazz, Object...params)
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
	public int count(Class<?> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.COUNT, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	/**
	 * 根据SQL查询一个整数
	 *
	 * @param <T> 类型参数
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 整数
	 */
	public <T> int queryForInt(String sql, T param)
	{
		AssertUtil.notEmpty(sql, "SQL");
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
	public int queryForInt(String sql, Map<String, Object> param)
	{
		AssertUtil.notEmpty(sql, "SQL");
		this.log(sql, param);

		Integer n = nJdbcTemplate.queryForObject(sql, param, Integer.class);
		return n == null ? 0 : n.intValue();
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @return 对象列表
	 */
	public <T> List<T> list(Class<T> clazz, String sql)
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
	public <T, P> List<T> list(Class<T> clazz, String sql, P param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notEmpty(sql, "SQL");
		Map<String, Object> map = ObjectUtil.toMap(param);
		return this.list(clazz, sql, map);
	}

	/**
	 * 根据SQL语句查询列表，以对象列表返回
	 *
	 * @param clazz 对象类型
	 * @param sql SQL语句
	 * @param param 查询参数
	 * @return 对象列表
	 */
	public <T> List<T> list(Class<T> clazz, String sql, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notEmpty(sql, "SQL");
		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.query(sql, param, mapper);
		return list;
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
	public <T> List<T> list(Class<T> clazz, String sql, int start, int size)
	{
		sql = dialect.page(sql, start, size);
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
	public <T, P> List<T> list(Class<T> clazz, String sql, P param, int start, int size)
	{
		sql = dialect.page(sql, start, size);
		return this.list(clazz, sql, param);
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
	public <T> List<T> list(Class<T> clazz, String sql, Map<String, Object> param, int start, int size)
	{
		sql = dialect.page(sql, start, size);
		return this.list(clazz, sql, param);
	}

	/**
	 * 查询字段列表
	 *
	 * @param clazz 对象类型
	 * @param target 字段类型
	 * @param field 字段名称
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 字段列表
	 */
	public <T> List<T> listField(Class<?> clazz, Class<T> target, String field, Object...params)
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
	public <T> List<T> listField(Class<?> clazz, Class<T> target, String field, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(target, "Target Class");
		AssertUtil.notEmpty(field, "field");

		String sql = factory.handleField(SqlType.GET_FIELD, clazz, field, param);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.queryForList(sql, param, target);
		return list;
	}

	/**
	 * 查询对象列表
	 *
	 * @param clazz 对象类型
	 * @param params [字段名0, 字段值0, 字段名1, 字段值1...]
	 * @return 对象列表
	 */
	public <T> List<T> listObject(Class<T> clazz, Object...params)
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
	public <T> List<T> listObject(Class<T> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");

		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		String sql = factory.handleMap(SqlType.GET, clazz, param);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.query(sql, param, mapper);
		return list;
	}

	/**
	 * 记录SQL 日志
	 * @param sql sql语句
	 */
	private void log(String sql, Object param)
	{
		if (debug)
		{
			logger.info("sql: {}, param: {}", sql, JsonUtil.output(param));
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("sql: {}, param: {}", sql, JsonUtil.output(param));
			}
		}
	}

	/**
	 * 根据类名查找表名
	 *
	 * @param clazz
	 * @return
	 */
	public String getTable(Class<?> clazz)
	{
		CacheEntity cacheEntity = parserCache.get(clazz);
		return cacheEntity.getTable();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (dataSource == null)
			throw new JdbcException("缺少 DataSource");
		if (packageList == null || packageList.isEmpty())
			throw new JdbcException("缺少 scanPackages");

		this.dialect = DetectDialect.dialect(dataSource);

		logger.info("扫描包：{}", packageList);
		ClassScanner scanner = new ClassScanner();
		scanner.setClassHandler(new EntityClassHandler());
		scanner.scan(true, packageList);
		this.factory = SqlHandlerFactory.init(dialect);
		factory.init(escape);
		CheckTable checkTable = new CheckTable(dataSource);
		checkTable.check();

		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.nJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getnJdbcTemplate()
	{
		return nJdbcTemplate;
	}

	public Dialect getDialect()
	{
		return dialect;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public boolean isEscape()
	{
		return escape;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public void setScanPackages(List<String> packages)
	{
		this.packageList = packages;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public void setEscape(boolean escape)
	{
		this.escape = escape;
	}

	public void setMachineIdProperties(String classpath)
	{
		MachineIdProvider provider = new MachineIdProvider(classpath);
		Integer id = provider.getMachineId();
		logger.info("当前机器ID：{}", id);
		this.snowflakeId = new SnowflakeId(id);
	}

}
