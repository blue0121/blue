package blue.internal.jdbc.util;

import blue.core.dict.DictParser;
import blue.core.dict.Dictionary;
import blue.core.util.BeanUtil;
import blue.internal.jdbc.parser.CacheEntity;
import blue.internal.jdbc.parser.CacheMapper;
import blue.internal.jdbc.parser.CacheVersion;
import blue.internal.jdbc.parser.ParserCache;
import blue.jdbc.exception.JdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhengj
 * @since 1.0 2016年9月10日
 */
public class ObjectRowMapper<T> implements RowMapper<T>
{
	private static Logger logger = LoggerFactory.getLogger(ObjectRowMapper.class);
	
	private Map<String, Method> setterMap = new HashMap<>();
	private Class<T> clazz;
	private ParserCache parser = ParserCache.getInstance();
	private DictParser dictParser = DictParser.getInstance();
	
	private static Map<Class<?>, RowMapper<?>> cache = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T> RowMapper<T> getInstance(Class<?> clazz)
	{
		return (RowMapper<T>)cache.computeIfAbsent(clazz, ObjectRowMapper::new);
	}
	

	public ObjectRowMapper(Class<T> clazz)
	{
		this.clazz = clazz;
		boolean found = parser.exist(clazz);
		if (found)
		{
			CacheEntity cacheEntity = parser.get(clazz);
			cacheEntity.getIdMap().forEach((k, v) -> this.setSetterMap(v.getColumn(), v.getSetterMethod()));
			cacheEntity.getColumnMap().forEach((k, v) -> this.setSetterMap(v.getColumn(), v.getSetterMethod()));
			cacheEntity.getExtraMap().forEach((k, v) -> this.setSetterMap(v.getColumn(), v.getSetterMethod()));
			if (cacheEntity.getVersion() != null)
			{
				CacheVersion cacheVersion = cacheEntity.getVersion();
				this.setSetterMap(cacheVersion.getColumn(), cacheVersion.getSetterMethod());
			}
		}
		if (!found)
		{
			found = parser.existMapper(clazz);
			if (found)
			{
				CacheMapper cacheMapper = parser.getMapper(clazz);
				cacheMapper.getColumnMap().forEach((k, v) -> this.setSetterMap(v.getColumn(), v.getSetterMethod()));
			}
		}
		if (!found)
		{
			throw new JdbcException(clazz.getName() + " 缺少 @Entity 或 @Mapper 注解");
		}
	}

	private void setSetterMap(String column, Method method)
	{
		setterMap.put(column.toLowerCase(), method);
		setterMap.put(column.toUpperCase(), method);
	}

	@Override
	public T mapRow(ResultSet rs, int row) throws SQLException
	{
		T object = BeanUtil.createBean(clazz, (Map<?, ?>)null);
		if (object == null)
			throw new JdbcException("无法实例化：" + clazz.getName());
		
		ResultSetMetaData data = rs.getMetaData();
		for (int i = 1; i <= data.getColumnCount(); i++)
		{
			String label = data.getColumnLabel(i);
			Method setter = setterMap.get(label);
			if (setter != null)
			{
				Class<?> type = setter.getParameterTypes()[0];
				Object value = this.convert(rs, i, type);
				try
				{
					setter.invoke(object, value);
				}
				catch (Exception e)
				{
					logger.error("Error, ", e);
					throw new JdbcException("无法设置属性：" + label);
				}
			}
		}
		
		return object;
	}

	private Object convert(ResultSet rs, int i, Class<?> type) throws SQLException
	{
		if (type == byte.class)
			return rs.getByte(i);
		else if (type == short.class)
			return rs.getShort(i);
		else if (type == int.class)
			return rs.getInt(i);
		else if (type == long.class)
			return rs.getLong(i);
		else if (type == float.class)
			return rs.getFloat(i);
		else if (type == double.class)
			return rs.getDouble(i);
		else if (type == Byte.class)
		{
			byte v = rs.getByte(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == Short.class)
		{
			short v = rs.getShort(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == Integer.class)
		{
			int v = rs.getInt(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == Long.class)
		{
			long v = rs.getLong(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == Float.class)
		{
			float v = rs.getFloat(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == Double.class)
		{
			double v = rs.getDouble(i);
			return rs.wasNull() ? null : v;
		}
		else if (type == BigDecimal.class)
			return rs.getBigDecimal(i);
		else if (type == String.class)
			return rs.getString(i);
		else if (type == java.sql.Date.class)
			return rs.getDate(i);
		else if (type == Time.class)
			return rs.getTime(i);
		else if (type == Timestamp.class || type == Date.class)
			return rs.getTimestamp(i);
		else if (type == Instant.class)
		{
			Timestamp timestamp = rs.getTimestamp(i);
			return timestamp == null ? null : timestamp.toInstant();
		}
		else if (type == LocalDate.class)
		{
			java.sql.Date date = rs.getDate(i);
			return date == null ? null : date.toLocalDate();
		}
		else if (type == LocalTime.class)
		{
			Time time = rs.getTime(i);
			return time == null ? null : time.toString();
		}
		else if (type == LocalDateTime.class)
		{
			Timestamp timestamp = rs.getTimestamp(i);
			return timestamp == null ? null : timestamp.toLocalDateTime();
		}
		else if (Dictionary.class.isAssignableFrom(type))
		{
			int index = rs.getInt(i);
			if (rs.wasNull())
				return null;
			
			return dictParser.getFromIndex((Class<Dictionary>)type, index);
		}
		
		return rs.getObject(i);
	}


}
