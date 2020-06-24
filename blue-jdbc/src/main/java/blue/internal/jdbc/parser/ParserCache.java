package blue.internal.jdbc.parser;

import blue.core.util.AssertUtil;
import blue.jdbc.exception.JdbcException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 所有Entity/Mapper解析缓存
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-25
 */
public class ParserCache
{
	private Map<Class<?>, CacheEntity> cache = new HashMap<>();
	private Map<Class<?>, CacheMapper> mapperCache = new HashMap<>();

	private static ParserCache instance = new ParserCache();

	private ParserCache()
	{
	}

	public static ParserCache getInstance()
	{
		return instance;
	}

	public CacheEntity get(Class<?> clazz)
	{
		AssertUtil.notNull(clazz, "Class");
		CacheEntity cacheEntity = cache.get(clazz);
		if (cacheEntity == null)
			throw new JdbcException(clazz.getName() + " 缺少 @Entity 注解");

		return cacheEntity;
	}

	public void put(CacheEntity entity, CacheMapper mapper)
	{
		if (entity != null)
		{
			cache.put(entity.getClazz(), entity);
		}
		if (mapper != null)
		{
			mapperCache.put(mapper.getClazz(), mapper);
		}
	}

	public boolean exist(Class<?> clazz)
	{
		AssertUtil.notNull(clazz, "Class");
		return cache.containsKey(clazz);
	}

	public CacheMapper getMapper(Class<?> clazz)
	{
		AssertUtil.notNull(clazz, "Class");
		CacheMapper cacheMapper = mapperCache.get(clazz);
		if (cacheMapper == null)
			throw new JdbcException(clazz.getName() + " 缺少 @Mapper 注解");

		return cacheMapper;
	}

	public boolean existMapper(Class<?> clazz)
	{
		AssertUtil.notNull(clazz, "Class");
		return mapperCache.containsKey(clazz);
	}

	public Set<Class<?>> allClazz()
	{
		return cache.keySet();
	}

}
