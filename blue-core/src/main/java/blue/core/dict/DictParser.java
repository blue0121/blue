package blue.core.dict;

import blue.core.util.AssertUtil;
import blue.internal.core.dict.DictConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典解析器
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public class DictParser
{
	private static Logger logger = LoggerFactory.getLogger(DictParser.class);
	private static DictParser parser = new DictParser();
	
	private Map<Class<?>, DictConfig> configMap = new ConcurrentHashMap<>();
	private Map<String, Map<String, String>> stringMap = new HashMap<>();
	
	public static DictParser getInstance()
	{
		return parser;
	}
	
	private DictParser()
	{
	}
	
	/**
	 * 根据字典索引值获取对象
	 * 
	 * @param clazz 字典类型
	 * @param index 索引值
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromIndex(Class<T> clazz, Integer index)
	{
		if (index == null)
			return null;
		
		AssertUtil.notNull(clazz, "字典类型");
		DictConfig config = this.parse(clazz);
		Map<Integer, Object> map = config.getIndexMap();
		if (map == null)
			return null;
		
		return (T)map.get(index);
	}

	/**
	 * 根据字典字段名称获取对象
	 *
	 * @param clazz 字典类型
	 * @param field 字段名称
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromField(Class<T> clazz, String field)
	{
		if (field == null || field.isEmpty())
			return null;

		AssertUtil.notNull(clazz, "字典类型");
		DictConfig config = this.parse(clazz);
		Map<String, Object> map = config.getFieldMap();
		if (map == null)
			return null;

		return (T)map.get(field);
	}

	/**
	 * 根据字典中文名称获取对象
	 *
	 * @param clazz 字典类型
	 * @param name 中文名称
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromName(Class<T> clazz, String name)
	{
		if (name == null || name.isEmpty())
			return null;

		AssertUtil.notNull(clazz, "字典类型");
		DictConfig config = this.parse(clazz);
		Map<String, Object> map = config.getNameMap();
		if (map == null)
			return null;

		return (T)map.get(name);
	}
	
	/**
	 * 根据对象获取字典索引值
	 * 
	 * @param object 对象
	 * @return 字典索引值
	 */
	public Integer getFromObject(Object object)
	{
		AssertUtil.notNull(object, "字典");
		Class<?> clazz = object.getClass();
		DictConfig config = this.parse(clazz);
		Map<Object, Integer> map = config.getObjectMap();
		if (map == null)
			return null;
		
		return map.get(object);
	}

	/**
	 * 根据对象获取字典中文名称
	 *
	 * @param object 对象
	 * @return 字典中文名称
	 */
	public String getNameFromObject(Dictionary object)
	{
		AssertUtil.notNull(object, "字典");
		Class<?> clazz = object.getClass();
		DictConfig config = this.parse(clazz);
		Map<Object, String> map = config.getObjectNameMap();
		if (map == null)
			return null;

		return map.get(object);
	}

	/**
	 * 根据对象获取字典字段名称
	 *
	 * @param object 对象
	 * @return 字典字段名称
	 */
	public String getFieldFromObject(Dictionary object)
	{
		AssertUtil.notNull(object, "字典");
		Class<?> clazz = object.getClass();
		DictConfig config = this.parse(clazz);
		Map<Object, String> map = config.getObjectFieldMap();
		if (map == null)
			return null;

		return map.get(object);
	}
	
	/**
	 * 获取所有字典键值对
	 * 
	 * @return 所有字典键值对
	 */
	public Map<String, Map<String, String>> getStringMap()
	{
		return stringMap;
	}
	
	/**
	 * 解析字典
	 * 
	 * @param clazz enum类型
	 */
	public DictConfig parse(Class<?> clazz)
	{
		DictConfig config = configMap.get(clazz);
		if (config != null)
			return config;
		
		config = this.parseDict(clazz);
		configMap.put(clazz, config);
		return config;
	}

	private DictConfig parseDict(Class<?> clazz)
	{
		if (!Dictionary.class.isAssignableFrom(clazz))
			throw new IllegalArgumentException(clazz.getName() + " 不是字典类型");
		
		DictConfig config = new DictConfig();
		config.setClazz(clazz);
		config.setName(clazz.getSimpleName());
		logger.info("mapped dict: {} <==> {}", config.getName(), clazz.getName());
		this.parseDictItem(config);
		return config;
	}

	private void parseDictItem(DictConfig config)
	{
		Class<?> clazz = config.getClazz();
		Map<Integer, Object> indexMap = new HashMap<>();
		Map<String, Object> fieldMap = new HashMap<>();
		Map<String, Object> nameMap = new HashMap<>();
		Map<Object, Integer> objectMap = new HashMap<>();
		Map<Object, String> objectFieldMap = new HashMap<>();
		Map<Object, String> objectNameMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		for (Field field : clazz.getFields())
		{
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
					&& Modifier.isStatic(field.getModifiers()) && field.getType() == clazz)
			{
				try
				{
					String fieldName = field.getName();
					Object object = field.get(null);
					if (!(object instanceof Dictionary))
						throw new IllegalArgumentException("字段 " + fieldName + " 不是字典类型");

					Dictionary dict = (Dictionary)object;
					int index = dict.getIndex();
					String name = dict.getName();
					indexMap.put(index, object);
					fieldMap.put(fieldName, object);
					nameMap.put(name, object);
					objectMap.put(object, index);
					objectFieldMap.put(object, fieldName);
					objectNameMap.put(object, name);
					map.put(String.valueOf(index), name);
					logger.debug("{}: {} : {} {} <==> {}", config.getName(), index, fieldName, name, object);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		config.setIndexMap(indexMap);
		config.setFieldMap(fieldMap);
		config.setNameMap(nameMap);
		config.setObjectMap(objectMap);
		config.setObjectFieldMap(objectFieldMap);
		config.setObjectNameMap(objectNameMap);
		if (stringMap.containsKey(config.getName()))
			throw new IllegalArgumentException("名称已经存在：" + config.getName());
		
		this.stringMap.put(config.getName(), map);
	}
	
}
