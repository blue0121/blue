package blue.internal.jdbc.util;

import blue.core.dict.DictParser;
import blue.core.dict.Dictionary;
import blue.internal.jdbc.parser.CacheColumn;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.CacheMapper;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.exception.JdbcException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class ObjectUtil
{
	private ObjectUtil()
	{
	}

	/**
	 * 对象转换为映射
	 *
	 * @param object
	 */
	public static Map<String, Object> toMap(Object object)
	{
		Map<String, Object> map = new HashMap<>();
		if (object == null)
			return map;

		boolean found = ParserCache.getInstance().exist(object.getClass());
		if (found)
		{
			CacheEntity cacheEntity = ParserCache.getInstance().get(object.getClass());
			try
			{
				toIdMap(object, map, cacheEntity.getIdMap());
				toColumnMap(object, map, cacheEntity.getColumnMap());
				toColumnMap(object, map, cacheEntity.getExtraMap());

				CacheVersion cacheVersion = cacheEntity.getVersion();
				if (cacheVersion != null)
				{
					Method getter = cacheVersion.getGetterMethod();
					Object val = getter.invoke(object);

					if (val == null)
					{
						val = 1;
					}

					map.put(cacheVersion.getName(), val);
				}
			}
			catch (Exception e)
			{
				throw new JdbcException(e);
			}
		}
		if (!found)
		{
			found = ParserCache.getInstance().existMapper(object.getClass());
			if (found)
			{
				CacheMapper cacheMapper = ParserCache.getInstance().getMapper(object.getClass());
				try
				{
					toColumnMap(object, map, cacheMapper.getColumnMap());
				}
				catch (Exception e)
				{
					throw new JdbcException(e);
				}
			}
		}
		if (!found)
		{
			throw new JdbcException(object.getClass().getName() + " 缺少 @Entity 或 @Mapper 注解");
		}
		return map;
	}

	private static void toIdMap(Object object, Map<String, Object> map,
	                            Map<String, CacheId> idMap) throws Exception
	{
		for (Map.Entry<String, CacheId> entry : idMap.entrySet())
		{
			CacheId cacheId = entry.getValue();
			Method getter = cacheId.getGetterMethod();
			Object val = convert(getter.invoke(object));
			map.put(cacheId.getName(), val);
		}
	}

	private static void toColumnMap(Object object, Map<String, Object> map,
	                                Map<String, CacheColumn> columnMap) throws Exception
	{
		for (Map.Entry<String, CacheColumn> entry : columnMap.entrySet())
		{
			CacheColumn cacheColumn = entry.getValue();
			Method getter = cacheColumn.getGetterMethod();
			Object val = convert(getter.invoke(object));
			map.put(cacheColumn.getName(), val);
		}
	}

	private static Object convert(Object val)
	{
		if (val == null)
			return null;

		Class<?> clazz = val.getClass();
		if (Dictionary.class.isAssignableFrom(clazz))
			return DictParser.getInstance().getFromObject(val);

		if ("".equals(val))
			return null;

		return val;
	}

	public static Map<String, Object> toParamMap(Object...params)
	{
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < params.length; i+=2)
		{
			map.put((String)params[i], params[i + 1]);
		}
		return map;
	}

	public static CacheEntity checkCacheEntity(Class<?> clazz)
	{
		ParserCache parserCache = ParserCache.getInstance();
		boolean existEntity = parserCache.exist(clazz);
		boolean existMapper = parserCache.existMapper(clazz);
		if (!existEntity)
		{
			if (existMapper)
				throw new JdbcException(clazz.getName() + " 需要覆写 select(), selectCount() 和 orderBy() 方法");
			else
				throw new JdbcException(clazz.getName() + " 缺少 @Entity 或 @Mapper 注解");
		}
		return parserCache.get(clazz);
	}

}
