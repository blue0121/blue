package blue.core.tree;

import blue.core.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 适配FreeMarker的Map工具类
 *
 * @author zhengjin
 * @since 1.0 2018年07月01日
 */
public class MapUtil
{
	private MapUtil()
	{
	}

	/**
	 * 把任意类型的Key，转化为字符串类型的Key，实现类为HashMap
	 *
	 * @param map 原始Map
	 * @return 字符串类型的Key
	 */
	public static <K, V> Map<String, V> toStringKey(Map<K, V> map)
	{
		return toStringKey(map, HashMap.class);
	}

	/**
	 * 把任意类型的Key，转化为字符串类型的Key
	 *
	 * @param map 原始Map
	 * @param mapClazz Map实现类
	 * @return 字符串类型的Key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<String, V> toStringKey(Map<K, V> map, Class<? extends Map> mapClazz)
	{
		AssertUtil.notNull(map, "原始Map");
		AssertUtil.notNull(mapClazz, "Map实现类");
		Map<String, V> stringMap = null;
		try
		{
			stringMap = (Map<String, V>) mapClazz.getDeclaredConstructor().newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (stringMap == null)
			throw new IllegalArgumentException("无法实例化：" + mapClazz.getName());

		for (Map.Entry<K, V> entry : map.entrySet())
		{
			if (entry.getKey() == null)
			{
				stringMap.put(null, entry.getValue());
			}
			else
			{
				stringMap.put(entry.getKey().toString(), entry.getValue());
			}
		}

		return stringMap;
	}

}
