package blue.internal.jdbc.core;

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
import blue.jdbc.core.Dialect;
import blue.jdbc.core.JdbcOperation;
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
public class JdbcObjectTemplate implements JdbcOperation, InitializingBean
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

	@Override
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

	@Override
	public int saveObject(Class<?> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.PARTIAL_INSERT, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.update(sql, param);
	}

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
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

	@Override
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

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
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

	@Override
	public int delete(Object object)
	{
		AssertUtil.notNull(object, "Object");
		CacheEntity cacheEntity = parserCache.get(object.getClass());
		String sql = cacheEntity.getDeleteSQL();
		Map<String, Object> source = IdUtil.getId(object);
		this.log(sql, source);

		return nJdbcTemplate.update(sql, source);
	}

	@Override
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

	@Override
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

	@Override
	public <T> int deleteBy(Class<T> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.DELETE, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.update(sql, param);
	}

	@Override
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

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@SuppressWarnings("varargs")
	@Override
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

	@Override
	public int count(Class<?> clazz, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		String sql = factory.handleMap(SqlType.COUNT, clazz, param);
		this.log(sql, param);
		return nJdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	@Override
	public int queryForInt(String sql, Map<String, Object> param)
	{
		AssertUtil.notEmpty(sql, "SQL");
		this.log(sql, param);

		Integer n = nJdbcTemplate.queryForObject(sql, param, Integer.class);
		return n == null ? 0 : n.intValue();
	}

	@Override
	public <T> List<T> list(Class<T> clazz, String sql, Map<String, Object> param)
	{
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notEmpty(sql, "SQL");
		RowMapper<T> mapper = ObjectRowMapper.getInstance(clazz);
		this.log(sql, param);
		List<T> list = nJdbcTemplate.query(sql, param, mapper);
		return list;
	}

	@Override
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

	@Override
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

	@Override
	public String getTable(Class<?> clazz, boolean escape)
	{
		CacheEntity cacheEntity = parserCache.get(clazz);
		return escape ? cacheEntity.getEscapeTable() : cacheEntity.getTable();
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

	@Override
	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	@Override
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate()
	{
		return nJdbcTemplate;
	}

	@Override
	public Dialect getDialect()
	{
		return dialect;
	}

	@Override
	public boolean isDebug()
	{
		return debug;
	}

	@Override
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
