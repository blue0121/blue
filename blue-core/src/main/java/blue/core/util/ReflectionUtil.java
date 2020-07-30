package blue.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 * 
 * @author zhengj
 * @since 1.0 2011-4-4
 */
public class ReflectionUtil
{
	private ReflectionUtil()
	{
	}

	/**
	 * 取得 field 映射，key为field_name，value为field
	 *
	 * @param clazz 类
	 * @param staticField 是否包含静态字段
	 * @return field 方法映射
	 */
	public static Map<String, Field> fieldMap(Class<?> clazz, boolean staticField)
	{
		Map<String, Field> fieldMap = new HashMap<>();
		for (Field field : clazz.getDeclaredFields())
		{
			if (Modifier.isFinal(field.getModifiers()))
				continue;

			if (!staticField && Modifier.isStatic(field.getModifiers()))
				continue;

			fieldMap.put(field.getName(), field);
		}

		return fieldMap;
	}
	
	/**
	 * 取得 getter 方法列表
	 * 
	 * @param clazz 类
	 * @return getter 方法列表
	 */
	public static List<Method> getterList(Class<?> clazz)
	{
		List<Method> methodList = new ArrayList<>();
		
		Method[] getters = clazz.getMethods();
		for (Method getter : getters)
		{
			String name = getter.getName();
			if (name.equals("getClass") || getter.getParameterTypes().length != 0)
				continue;
			
			Class<?> returnClazz = getter.getReturnType();
			
			if (name.startsWith("is") && (returnClazz == Boolean.class || returnClazz == boolean.class))
				methodList.add(getter);
			else if (name.startsWith("get"))
				methodList.add(getter);
		}
		
		return methodList;
	}
	
	/**
	 * 取得 getter 方法映射，key为field_name，value为getter_method
	 * 
	 * @param clazz 类
	 * @return getter 方法映射
	 */
	public static Map<String, Method> getterMap(Class<?> clazz)
	{
		List<Method> getterList = getterList(clazz);
		Map<String, Method> getterMap = new HashMap<>();
		for (Method getter : getterList)
		{
			String field = field(getter);
			getterMap.put(field, getter);
		}
		return getterMap;
	}
	
	/**
	 * 取得 setter 方法列表
	 * 
	 * @param clazz 类
	 * @return setter 方法列表
	 */
	public static List<Method> setterList(Class<?> clazz)
	{
		List<Method> methodList = new ArrayList<Method>();
		
		Method[] setters = clazz.getMethods();
		for (Method setter : setters)
		{
			String name = setter.getName();
			if (name.startsWith("set") && setter.getParameterTypes().length == 1)
			{
				methodList.add(setter);
			}
			
		}
		
		return methodList;
	}
	
	/**
	 * 取得 setter 方法映射，key为field_name，value为setter_method
	 * 
	 * @param clazz 类
	 * @return setter 方法映射
	 */
	public static Map<String, Method> setterMap(Class<?> clazz)
	{
		List<Method> setterList = setterList(clazz);
		Map<String, Method> setterMap = new HashMap<>();
		for (Method setter : setterList)
		{
			String field = field(setter);
			setterMap.put(field, setter);
		}
		return setterMap;
	}
	
	
	/**
	 * 取得 getter 方法
	 * 
	 * @param clazz 类
	 * @param field 字段
	 * @return getter 方法
	 */
	public static Method getter(Class<?> clazz, Field field)
	{
		String prefix = field.getName().substring(0, 1).toUpperCase();
		String name = prefix + field.getName().substring(1);
		if (field.getType() == Boolean.class || field.getType() == boolean.class)
		{
			name = "is" + name;
		}
		else
		{
			name = "get" + name;
		}
		try
		{
			Method method = clazz.getMethod(name);
			return method;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 取得 setter 方法
	 * 
	 * @param clazz 类
	 * @param field 字段
	 * @return setter 方法
	 */
	public static Method setter(Class<?> clazz, Field field)
	{
		String prefix = field.getName().substring(0, 1).toUpperCase();
		String name = "set" + prefix + field.getName().substring(1);
		try
		{
			Method method = clazz.getMethod(name, field.getType());
			return method;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 取得 getter 方法
	 * 
	 * @param clazz 类
	 * @param setter 方法
	 * @return getter 方法
	 */
	public static Method getter(Class<?> clazz, Method setter)
	{
		String name = setter.getName().substring(3);
		String getter = name.substring(0, 1).toUpperCase() + name.substring(1);
		Class<?> type = setter.getParameterTypes()[0];
		if (type == Boolean.class || type == boolean.class)
		{
			getter = "is" + getter;
		}
		else
		{
			getter = "get" + getter;
		}
		try
		{
			Method method = clazz.getMethod(getter);
			return method;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 取得 setter 方法
	 * 
	 * @param clazz 类
	 * @param getter 方法
	 * @return setter 方法
	 */
	public static Method setter(Class<?> clazz, Method getter)
	{
		Class<?> type = getter.getReturnType();
		String setter = null;
		if (type == Boolean.class || type == boolean.class)
		{
			String name = getter.getName().substring(2);
			setter = "set" + name;
		}
		else
		{
			String name = getter.getName().substring(3);
			setter = "set" + name;
		}
		try
		{
			Method method = clazz.getMethod(setter, type);
			return method;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 从 setter/getter 方法取得字段
	 * 
	 * @param setter/getter 方法
	 * @return field 字段
	 */
	public static String field(Method setter)
	{
		String name = null;
		if (setter.getName().startsWith("is"))
		{
			name = setter.getName().substring(2);
		}
		else
		{
			name = setter.getName().substring(3);
		}
		String field = name.substring(0, 1).toLowerCase() + name.substring(1);
		
		return field;
	}

	/**
	 * 从 setter/getter 方法名称取得字段名称
	 *
	 * @param methodName getter/getter 方法名称
	 * @return field 字段
	 */
	public static String field(String methodName)
	{
		String name = null;
		if (methodName.startsWith("is"))
		{
			name = methodName.substring(2);
		}
		else if (methodName.startsWith("set") || methodName.startsWith("get"))
		{
			name = methodName.substring(3);
		}
		if (name == null || name.isEmpty())
			return null;

		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	/**
	 * 获取对象的名称
	 * 
	 * @param object 对象
	 * @return 名称
	 */
	public static String getName(Object object)
	{
		Class<?> clazz = object.getClass();
		Method method = null;
		try
		{
			method = clazz.getMethod("getName");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (method == null)
			return object.toString();
		
		Class<?> retType = method.getReturnType();
		if (retType != String.class)
			throw new IllegalArgumentException("索引方法返回值不是字符串");
		
		try
		{
			Object ret = method.invoke(object);
			return (String)ret;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("名称方法调用错误");
		}
	}
	
	/**
	 * 获取对象的索引值
	 * 
	 * @param object 对象
	 * @return 索引值
	 */
	public static Integer getIndex(Object object)
	{
		Class<?> clazz = object.getClass();
		Method method = null;
		try
		{
			method = clazz.getMethod("getIndex");
		}
		catch (Exception e)
		{
			try
			{
				method = clazz.getMethod("ordinal");
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		if (method == null)
			throw new IllegalArgumentException("找不到索引方法：getIndex() | ordinal()");
		
		Class<?> retType = method.getReturnType();
		if (retType != Integer.class && retType != int.class)
			throw new IllegalArgumentException("索引方法返回值不是整型");
		
		try
		{
			Object ret = method.invoke(object);
			return (Integer)ret;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("索引方法调用错误");
		}
	}
	
	/**
	 * 枚举字段名 ==> 枚举
	 * 
	 * @param clazz 枚举的类型
	 * @param name 枚举字段名
	 * @return 枚举
	 */
	public static Object valueOfEnum(Class<?> clazz, String name)
	{
		AssertUtil.notNull(clazz, "class");
		if (!clazz.isEnum())
			throw new IllegalArgumentException("类型不是枚举类型");
		
		Method method = null;
		try
		{
			method = clazz.getMethod("valueOf");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (method == null)
			throw new IllegalArgumentException("找不到方法：valueOf()");
		
		try
		{
			return method.invoke(null, name);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("valueOf() 方法调用错误");
		}
	}
	
	/**
	 * 把对象字段名称转化为表字段名称
	 * 
	 * @param field 对象字段名称
	 * @return 表字段名称
	 */
	public static String fieldToColumn(String field)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < field.length(); i++)
		{
			char c = field.charAt(i);
			if (Character.isUpperCase(c))
			{
				if (i > 0)
					sb.append('_');
				
				sb.append(Character.toLowerCase(c));
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 把表字段名称转化为对象字段名称
	 * 
	 * @param column 表字段名称
	 * @return 对象字段名称
	 */
	public static String columnToField(String column)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < column.length(); i++)
		{
			char c = column.charAt(i);
			if (c == '_')
			{
				if (i > 0 && i < column.length() - 1)
				{
					char n = column.charAt(++i);
					sb.append(Character.toUpperCase(n));
				}
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 把表名称转化为对象类名称
	 * 
	 * @param table 表名称
	 * @return 对象类名称
	 */
	public static String tableToClazz(String table, boolean prefix)
	{
		StringBuilder sb = new StringBuilder(table.length());
		boolean pre = !prefix;
		for (int i = 0; i < table.length(); i++)
		{
			char c = table.charAt(i);
			if (c == '_')
			{
				pre = true;
				if (i < table.length() - 1)
				{
					char n = table.charAt(++i);
					sb.append(Character.toUpperCase(n));
				}
			}
			else
			{
				if (pre)
				{
					if (i == 0)
					{
						sb.append(Character.toUpperCase(c));
					}
					else
					{
						sb.append(c);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 把类名称转化为字段类名称
	 * 
	 * @param clazz 类名称
	 * @return 字段名称
	 */
	public static String clazzToField(String clazz)
	{
		AssertUtil.notEmpty(clazz, "类名称");
		StringBuilder sb = new StringBuilder(clazz.length());
		for (int i = 0; i < clazz.length(); i++)
		{
			char c = clazz.charAt(i);
			if (i == 0)
				sb.append(Character.toLowerCase(c));
			else
				sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 把字段名称转化为类名称
	 * 
	 * @param field 字段名称
	 * @return 类名称
	 */
	public static String fieldToClazz(String field)
	{
		AssertUtil.notEmpty(field, "字段名称");
		StringBuilder sb = new StringBuilder(field.length());
		for (int i = 0; i < field.length(); i++)
		{
			char c = field.charAt(i);
			if (i == 0)
				sb.append(Character.toUpperCase(c));
			else
				sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 把字段名称转化为 getter 方法名称
	 * 
	 * @param field 字段名称
	 * @return getter 方法名称
	 */
	public static String fieldToGetter(String field)
	{
		String prefix = field.substring(0, 1).toUpperCase();
		String name = "get" + prefix + field.substring(1);
		return name;
	}
	
	/**
	 * 把字段名称转化为 setter 方法名称
	 * 
	 * @param field 字段名称
	 * @return setter 方法名称
	 */
	public static String fieldToSetter(String field)
	{
		String prefix = field.substring(0, 1).toUpperCase();
		String name = "set" + prefix + field.substring(1);
		return name;
	}

}
