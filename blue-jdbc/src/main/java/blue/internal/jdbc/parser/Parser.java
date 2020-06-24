package blue.internal.jdbc.parser;

import blue.core.util.ReflectionUtil;
import blue.jdbc.annotation.Column;
import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.GeneratorType;
import blue.jdbc.annotation.Id;
import blue.jdbc.annotation.IdType;
import blue.jdbc.annotation.Mapper;
import blue.jdbc.annotation.Must;
import blue.jdbc.annotation.Transient;
import blue.jdbc.annotation.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象解析类
 * 
 * @author zhengj
 * @since 2016年7月9日 1.0
 */
public class Parser
{
	private static Logger log = LoggerFactory.getLogger(Parser.class);

	private ParserCache cache = ParserCache.getInstance();
	
	private static Parser instance = new Parser();

	private Parser()
	{
	}
	
	public static Parser getInstance()
	{
		return instance;
	}
	
	/**
	 * 解析
	 * 
	 * @param clazz
	 * @return
	 */
	public void parse(Class<?> clazz)
	{
		Entity annoEntity = clazz.getAnnotation(Entity.class);
		if (annoEntity != null)
		{
			if (!cache.exist(clazz))
			{
				CacheEntity entity = this.parseEntity(clazz, annoEntity);
				cache.put(entity, null);
			}
			return;
		}

		Mapper annoMapper = clazz.getAnnotation(Mapper.class);
		if (annoMapper != null)
		{
			if (!cache.existMapper(clazz))
			{
				CacheMapper mapper = this.parseMapper(clazz);
				cache.put(null, mapper);
			}
			return;
		}

		throw new IllegalArgumentException(clazz + "缺少 @Entity 或 @Mapper 注解");
	}


	private CacheMapper parseMapper(Class<?> clazz)
	{
		CacheMapper mapper = new CacheMapper();
		mapper.setClazz(clazz);
		log.info("mapped class: {}", clazz.getName());

		Map<String, Field> fieldMap = new HashMap<>();
		Map<String, Method> setterMap = new HashMap<>();
		Map<String, Method> getterMap = new HashMap<>();
		this.initMember(clazz, fieldMap, setterMap, getterMap);

		for (Map.Entry<String, Method> entry : setterMap.entrySet())
		{
			String fieldName = entry.getKey();
			Field field = fieldMap.get(fieldName);
			if (field != null) // 类本身字段
			{
				CacheColumn cacheColumn = this.parseFieldColumn(field, setterMap, getterMap);
				mapper.putColumn(cacheColumn);
				log.debug("column: {} <==> {}", field.getName(), cacheColumn.getColumn());
			}
		}

		return mapper;
	}

	/**
	 * 解析实体
	 * 
	 * @param clazz
	 * @return
	 */
	private CacheEntity parseEntity(Class<?> clazz, Entity annoEntity)
	{
		CacheEntity cacheEntity = new CacheEntity();
		cacheEntity.setClazz(clazz);
		
		String table = (annoEntity.table().isEmpty() ? ReflectionUtil.fieldToColumn(clazz.getSimpleName()) : annoEntity.table());
		cacheEntity.setTable(table);
		cacheEntity.setEscapeTable(table);
		
		log.info("mapped entity class: {} <==> {}", clazz.getName(), table);
		
		this.parseField(cacheEntity, clazz);

		if (cacheEntity.getIdMap().isEmpty())
		{
			log.warn("no primary key/id field, class: {}", clazz.getName());
		}
		
		return cacheEntity;
	}
	
	/**
	 * 解析字段
	 * 
	 * @param cacheEntity
	 * @param clazz
	 */
	private  void parseField(CacheEntity cacheEntity, Class<?> clazz)
	{
		Map<String, Field> fieldMap = new HashMap<>();
		Map<String, Method> setterMap = new HashMap<>();
		Map<String, Method> getterMap = new HashMap<>();
		
		this.initMember(clazz, fieldMap, setterMap, getterMap);
		
		for (Map.Entry<String, Method> entry : setterMap.entrySet())
		{
			String fieldName = entry.getKey();
			Field field = fieldMap.get(fieldName);
			if (field != null) // 类本身字段
			{
				Id annoId = field.getAnnotation(Id.class);
				if (annoId != null) // 是主键
				{
					CacheId cacheId = this.parseFieldId(field, annoId, setterMap, getterMap);
					log.debug("id: {} <==> {}", field.getName(), cacheId.getColumn());
					cacheEntity.putId(cacheId);
					continue;
				}
				
				Version annoVersion = field.getAnnotation(Version.class);
				if (annoVersion != null) // 版本
				{
					if (cacheEntity.getVersion() != null)
						throw new IllegalArgumentException(clazz.getName() + "最多只能有一个 @Version");

					CacheVersion cacheVersion = this.parseFieldVersion(field, annoVersion, setterMap, getterMap);
					log.debug("version: {} <==> {}", field.getName(), cacheVersion.getColumn());
					cacheEntity.setVersion(cacheVersion);
					continue;
				}
				
				// 字段
				CacheColumn cacheColumn = this.parseFieldColumn(field, setterMap, getterMap);
				if (field.getAnnotation(Transient.class) == null)
				{
					log.debug("column: {} <==> {}", field.getName(), cacheColumn.getColumn());
					cacheEntity.putColumn(cacheColumn);
				}
				else
				{
					log.debug("extra: {} <==> {}", field.getName(), cacheColumn.getColumn());
					cacheEntity.putExtra(cacheColumn);
				}
			}
			else // 继承额外字段
			{
				CacheColumn cacheColumn = this.parseFieldColumn(field, setterMap, getterMap);
				cacheEntity.putExtra(cacheColumn);
			}
		}
		
	}

	private void initMember(Class<?> clazz, Map<String, Field> fieldMap,
	                        Map<String, Method> setterMap, Map<String, Method> getterMap)
	{
		for (Field field : clazz.getDeclaredFields())
		{
			fieldMap.put(field.getName(), field);
		}
		for (Method setter : ReflectionUtil.setterList(clazz))
		{
			setterMap.put(ReflectionUtil.field(setter), setter);
		}
		for (Method getter : ReflectionUtil.getterList(clazz))
		{
			getterMap.put(ReflectionUtil.field(getter), getter);
		}
	}

	private CacheId parseFieldId(Field field, Id annoId, Map<String, Method> setterMap,
	                             Map<String, Method> getterMap)
	{
		String column = this.getColumn(field, setterMap, getterMap);
		
		CacheId cacheId = new CacheId();
		cacheId.setField(field);
		cacheId.setName(field.getName());
		cacheId.setSetterMethod(setterMap.get(field.getName()));
		cacheId.setGetterMethod(getterMap.get(field.getName()));
		cacheId.setColumn(column);
		cacheId.setEscapeColumn(column);
		if (field.getType() == String.class)
		{
			cacheId.setIdType(IdType.STRING);
		}
		else if (field.getType() == Integer.class || field.getType() == int.class)
		{
			cacheId.setIdType(IdType.INT);
		}
		else if (field.getType() == Long.class || field.getType() == long.class)
		{
			cacheId.setIdType(IdType.LONG);
		}
		else
		{
			throw new IllegalArgumentException("主键不支持类型：" + field.getType().getName());
		}
		
		GeneratorType type = annoId.generator();
		if (type == GeneratorType.AUTO)
		{
			if (cacheId.getIdType() == IdType.STRING)
				cacheId.setGeneratorType(GeneratorType.UUID);
			else
				cacheId.setGeneratorType(GeneratorType.INCREMENT);
		}
		else
		{
			cacheId.setGeneratorType(type);
		}
		return cacheId;
	}
	
	private CacheVersion parseFieldVersion(Field field, Version annoVersion, Map<String, Method> setterMap,
	                                       Map<String, Method> getterMap)
	{
		String column = this.getColumn(field, setterMap, getterMap);
		
		if (field.getType() != int.class && field.getType() != Integer.class
				&& field.getType() != long.class && field.getType() != Long.class)
			throw new IllegalArgumentException(field.getName() + " 必须是 int 或 long 类型");
		
		CacheVersion cacheVersion = new CacheVersion();
		cacheVersion.setField(field);
		cacheVersion.setGetterMethod(getterMap.get(field.getName()));
		cacheVersion.setSetterMethod(setterMap.get(field.getName()));
		cacheVersion.setName(field.getName());
		cacheVersion.setColumn(column);
		cacheVersion.setEscapeColumn(column);
		cacheVersion.setForce(annoVersion.force());
		return cacheVersion;
	}

	private CacheColumn parseFieldColumn(Field field, Map<String, Method> setterMap,
	                                     Map<String, Method> getterMap)
	{
		String column = this.getColumn(field, setterMap, getterMap);
		
		CacheColumn cacheColumn = new CacheColumn();
		cacheColumn.setField(field);
		cacheColumn.setGetterMethod(getterMap.get(field.getName()));
		cacheColumn.setSetterMethod(setterMap.get(field.getName()));
		cacheColumn.setName(field.getName());
		cacheColumn.setColumn(column);
		cacheColumn.setEscapeColumn(column);
		
		Must annoMust = field.getAnnotation(Must.class);
		if (annoMust != null)
		{
			cacheColumn.setMustInsert(annoMust.insert());
			cacheColumn.setMustUpdate(annoMust.update());
		}
		return cacheColumn;
	}

	private String getColumn(Field field, Map<String, Method> setterMap, Map<String, Method> getterMap)
	{
		Column annoColumn = field.getAnnotation(Column.class);
		String column = (annoColumn == null ? ReflectionUtil.fieldToColumn(field.getName()) : annoColumn.name());

		Method setter = setterMap.get(field.getName());
		if (setter == null)
			throw new IllegalArgumentException(field.getName() + " 不存在 setter 方法");

		Method getter = getterMap.get(field.getName());
		if (getter == null)
			throw new IllegalArgumentException(field.getName() + " 不存在 getter 方法");

		return column;
	}
	
}
