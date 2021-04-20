package blue.base.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 适配FreeMarker的Map工具类
 *
 * @author zhengjin
 * @since 1.0 2018年07月01日
 */
public class MapUtil {
	private MapUtil() {
	}

	/**
	 * 把任意类型的Key，转化为字符串类型的Key，实现类为HashMap
	 *
	 * @param map 原始Map
	 * @return 字符串类型的Key
	 */
	public static <K, V> Map<String, V> toStringKey(Map<K, V> map) {
		return toStringKey(map, new HashMap<>());
	}

	/**
	 * 把任意类型的Key，转化为字符串类型的Key
	 *
	 * @param map       原始Map
	 * @param stringMap Map实现类
	 * @return 字符串类型的Key
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <K, V> Map<String, V> toStringKey(Map<K, V> map, Map<String, V> stringMap) {
		AssertUtil.notNull(map, "原始Map");
		AssertUtil.notNull(stringMap, "Map实现类");

		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (entry.getKey() == null) {
				stringMap.put(null, entry.getValue());
			}
			else {
				stringMap.put(entry.getKey().toString(), entry.getValue());
			}
		}

		return stringMap;
	}

}
