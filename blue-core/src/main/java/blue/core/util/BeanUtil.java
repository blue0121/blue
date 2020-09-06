package blue.core.util;

import blue.core.convert.ConvertService;
import blue.core.convert.ConvertServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JavaBean工具类
 *
 * @author zhengj
 * @since 1.0 2014-1-19
 */
public class BeanUtil
{
	private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	private BeanUtil()
	{
	}

	/**
	 * 把Map列表转换成对象列表
	 *
	 * @param clazz   目标对象类型
	 * @param mapList Map列表
	 * @return 目标对象列表
	 */
	public static <T> List<T> createBean(Class<T> clazz, List<Map<String, ?>> mapList)
	{
		List<T> list = new ArrayList<>();
		if (mapList == null || mapList.isEmpty())
			return list;

		for (Map<String, ?> map : mapList)
		{
			T obj = createBean(clazz, map);
			if (obj == null)
				continue;

			list.add(obj);
		}
		return list;
	}

	/**
	 * 把Map转换成对象
	 *
	 * @param clazz 目标对象类型
	 * @param map   Map
	 * @return 目标对象
	 */
	public static <T> T createBean(Class<T> clazz, Map<String, ?> map)
	{
		AssertUtil.notNull(clazz, "class");
		T obj = null;
		try
		{
			obj = clazz.getDeclaredConstructor().newInstance();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		if (obj == null)
			return null;

		if (map == null || map.isEmpty())
			return obj;

		ConvertService convertService = ConvertServiceFactory.getConvertService();
		for (Map.Entry<String, Method> entry : ReflectionUtil.setterMap(clazz).entrySet())
		{
			Object value = map.get(entry.getKey());
			if (value == null || value.equals(""))
				continue;

			Method setter = entry.getValue();
			Class<?>[] params = setter.getParameterTypes();
			if (params.length != 1)
				continue;

			/*Class<?> param = params[0];
			Object val = convert(param, value);*/
			Object val = convertService.convert(value, params[0]);
			try
			{
				setter.invoke(obj, val);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return obj;
	}

	/**
	 * 把Object对象列表的字符复制到新对象列表中
	 *
	 * @param clazz      对象类型
	 * @param objectList 原对象列表
	 * @return 新对象列表
	 */
	public static <T> List<T> createBeanList(Class<T> clazz, List<?> objectList)
	{
		List<T> list = new ArrayList<>();
		if (objectList == null || objectList.isEmpty())
		{
			return list;
		}

		for (Object object : objectList)
		{
			list.add(createBean(clazz, object));
		}
		return list;
	}

	/**
	 * 把Object对象的字段值复制到新对象中
	 *
	 * @param clazz  新对象的类型
	 * @param object 原对象
	 * @return 新对象
	 */
	public static <T> T createBean(Class<T> clazz, Object object)
	{
		AssertUtil.notNull(clazz, "class");
		T target = null;
		try
		{
			target = clazz.getDeclaredConstructor().newInstance();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		if (target == null)
			return null;

		if (object == null)
			return target;

		Map<String, Method> setterMap = ReflectionUtil.setterMap(clazz);
		Map<String, Method> getterMap = ReflectionUtil.getterMap(object.getClass());
		ConvertService convertService = ConvertServiceFactory.getConvertService();
		for (Map.Entry<String, Method> entry : getterMap.entrySet())
		{
			Method setter = setterMap.get(entry.getKey());
			if (setter == null)
				continue;

			try
			{
				Object value = entry.getValue().invoke(object);
				if (value == null)
					continue;

				Class<?> param = setter.getParameterTypes()[0];
				//Object cvtValue = convert(param, value);
				Object cvtValue = convertService.convert(value, param);

				if (cvtValue.getClass() == param || param.isAssignableFrom(cvtValue.getClass()))
				{
					setter.invoke(target, cvtValue);
				}
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}

		return target;
	}

	/**
	 * 使用Spring的IoC容器实例化并初始化指定类
	 *
	 * @param clazz  类
	 * @param target 目标类型
	 * @param ctx    Spring上下文对象
	 * @return 实例化后的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initBean(Class<?> clazz, Class<T> target, ApplicationContext ctx)
	{
		String className = clazz.getName();
		T obj = null;
		try
		{
			obj = (T) clazz.getDeclaredConstructor().newInstance();
			inject(obj, clazz, ctx);
			init(obj);
		}
		catch (SecurityException e)
		{
			logger.error("无法访问类：" + className, e);
		}
		catch (InstantiationException e)
		{
			logger.error("无法实例化类，可能缺少无参构造方法：" + className, e);
		}
		catch (IllegalAccessException e)
		{
			logger.error("无法访问：" + className, e);
		}
		catch (Exception e)
		{
			logger.error("InitializingBean.afterPropertiesSet()抛出", e);
		}

		return obj;
	}

	/**
	 * 使用Spring的IoC容器实例化并初始化指定类
	 *
	 * @param clazz 类
	 * @param ctx   Spring上下文对象
	 * @return 实例化后的类
	 */
	public static <T> T initBean(Class<T> clazz, ApplicationContext ctx)
	{
		return initBean(clazz, clazz, ctx);
	}

	/**
	 * 使用Spring的IoC容器实例化并初始化指定类
	 *
	 * @param className 类名
	 * @param target    目标类型
	 * @param ctx       Spring上下文对象
	 * @return 实例化后的类
	 */
	public static <T> T initBean(String className, Class<T> target, ApplicationContext ctx)
	{
		T obj = null;
		try
		{
			Class<?> clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
			obj = initBean(clazz, target, ctx);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("找不到类：" + className, e);
		}

		return obj;
	}


	/**
	 * 实例化指定的类
	 *
	 * @param <T>       类型参数
	 * @param className 类
	 * @param target    目标类型
	 * @return 实例化后的类
	 * @throws ClassNotFoundException 找不到类时抛出
	 * @throws InstantiationException 无法实例化时抛出
	 * @throws IllegalAccessException 无法访问时抛出
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String className, Class<T> target) throws Exception
	{
		Class<?> clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		Object obj = clazz.getDeclaredConstructor().newInstance();
		return (T) obj;
	}

	/**
	 * 调用 Bean初始化 方法
	 *
	 * @param target 目标对象
	 * @throws Exception InitializingBean.afterPropertiesSet()抛出
	 */
	public static void init(Object target) throws Exception
	{
		if (target instanceof InitializingBean)
		{
			InitializingBean initBean = (InitializingBean) target;
			initBean.afterPropertiesSet();
		}
	}

	/**
	 * 注入属性
	 *
	 * @param target 目标对象
	 * @param ctx    Spring上下文对象
	 * @throws SecurityException      类安全问题时抛出
	 * @throws IllegalAccessException 无法访问类属性时抛出
	 */
	public static void inject(Object target, ApplicationContext ctx)
			throws SecurityException, IllegalAccessException
	{
		Class<?> clazz = target.getClass();
		inject(target, clazz, ctx);
	}

	private static void inject(Object target, Class<?> clazz, ApplicationContext ctx)
			throws SecurityException, IllegalAccessException
	{
		if (clazz == Object.class)
		{
			return;
		}

		Field[] fields = clazz.getDeclaredFields();
		Field.setAccessible(fields, true);
		for (Field field : fields)
		{
			Resource resourceAnno = field.getAnnotation(Resource.class);
			if (resourceAnno != null)
			{
				logger.debug(field.getName() + " 有 Reource 注解");
				setFieldValue(target, ctx, field, resourceAnno);
				continue;
			}

			Autowired autowiredAnno = field.getAnnotation(Autowired.class);
			if (autowiredAnno != null)
			{
				logger.debug(field.getName() + " 有 Autowired 注解");
				setFieldValue(target, ctx, field, autowiredAnno);
			}
		}
		inject(target, clazz.getSuperclass(), ctx);
	}

	private static void setFieldValue(Object target, ApplicationContext ctx, Field field,
	                                  Resource resourceAnno) throws IllegalAccessException
	{
		String name = resourceAnno.name();
		Class<?> fieldClass = field.getType();
		Object fieldValue = null;
		if (name == null || name.equals(""))
		{
			name = field.getName();
			fieldValue = ctx.getBean(name, fieldClass);
			if (fieldValue == null)
			{
				fieldValue = ctx.getBean(fieldClass);
			}

		}
		else
		{
			fieldValue = ctx.getBean(name, fieldClass);
		}
		if (fieldValue == null)
		{
			throw new IllegalAccessException(target.getClass().getName() + " 中的 "
					+ field.getName() + " 属性无注入");
		}

		field.set(target, fieldValue);
	}

	private static void setFieldValue(Object target, ApplicationContext ctx, Field field,
	                                  Autowired autowiredAnno) throws IllegalAccessException
	{
		Qualifier qualifierAnno = field.getAnnotation(Qualifier.class);
		Class<?> fieldClass = field.getType();
		Object fieldValue = null;
		if (qualifierAnno == null)
		{
			fieldValue = ctx.getBean(fieldClass);
		}
		else
		{
			logger.debug(field.getName() + " 有 Qualifier 注解");
			String name = qualifierAnno.value();
			if (name == null || name.equals(""))
			{
				name = field.getName();
				fieldValue = ctx.getBean(name, fieldClass);
				if (fieldValue == null)
				{
					fieldValue = ctx.getBean(fieldClass);
				}
			}
			else
			{
				fieldValue = ctx.getBean(name, fieldClass);
			}
		}
		if (autowiredAnno.required() && fieldValue == null)
		{
			throw new IllegalAccessException(target.getClass().getName() + " 中的 "
					+ field.getName() + " 属性无注入");
		}

		if (fieldValue != null)
		{
			field.set(target, fieldValue);
		}
	}
}
