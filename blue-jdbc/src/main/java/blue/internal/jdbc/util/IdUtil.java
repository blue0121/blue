package blue.internal.jdbc.util;

import blue.core.id.IdGenerator;
import blue.core.id.SnowflakeId;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheId;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.annotation.GeneratorType;
import blue.jdbc.annotation.IdType;
import blue.jdbc.exception.JdbcException;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 主键工具类
 *
 * @author Jin Zheng
 * @since 2019-11-24
 */
public class IdUtil
{
	private IdUtil()
	{
	}

	/**
	 * 产生主键
	 *
	 * @param clazz
	 * @return
	 */
	public static Map<String, Object> generateId(Class<?> clazz)
	{
		return generateId(clazz, null);
	}

	public static Map<String, Object> generateId(Class<?> clazz, SnowflakeId snowflakeId)
	{
		CacheEntity cacheEntity = ParserCache.getInstance().get(clazz);
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			if (id.getGeneratorType() == GeneratorType.UUID)
			{
				if (id.getIdType() == IdType.STRING)
				{
					String uuid = IdGenerator.uuid32bit();
					map.put(id.getName(), uuid);
				}
				else if (id.getIdType() == IdType.LONG)
				{
					if (snowflakeId == null)
					{
						map.put(id.getName(), IdGenerator.id());
					}
					else
					{
						map.put(id.getName(), snowflakeId.nextId());
					}
				}
			}
		}
		return map;
	}

	/**
	 * 获取对象的主键
	 *
	 * @param obj 对象实例
	 * @return 映射
	 */
	public static Map<String, Object> getId(Object obj)
	{
		Map<String, Object> map = new HashMap<>();
		CacheEntity cacheEntity = ParserCache.getInstance().get(obj.getClass());
		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			try
			{
				Method getter = id.getGetterMethod();
				map.put(id.getName(), getter.invoke(obj));
			}
			catch (Exception e)
			{
				throw new JdbcException(e);
			}
		}
		return map;
	}

	/**
	 * 设置对象主键
	 *
	 * @param key
	 * @param object
	 */
	public static void setId(KeyHolder key, Object object)
	{
		if (key == null || key.getKey() == null)
			return;

		CacheEntity cacheEntity = ParserCache.getInstance().get(object.getClass());
		for (Map.Entry<String, CacheId> entry : cacheEntity.getIdMap().entrySet())
		{
			CacheId id = entry.getValue();
			try
			{
				if (id.getGeneratorType() == GeneratorType.INCREMENT)
				{
					Method setter = id.getSetterMethod();
					Number nid = key.getKey();
					if (id.getIdType() == IdType.LONG)
					{
						setter.invoke(object, nid.longValue());
					}
					else if (id.getIdType() == IdType.INT)
					{
						setter.invoke(object, nid.intValue());
					}
				}
			}
			catch (Exception e)
			{
				throw new JdbcException(e);
			}
		}

	}

	public static CacheId checkSingleId(CacheEntity cacheEntity)
	{
		CacheId cacheId = cacheEntity.getId();
		if (cacheId == null)
			throw new JdbcException(cacheEntity.getClazz().getName() + " 主键个数不为1，不支持该操作");

		return cacheId;
	}

}
