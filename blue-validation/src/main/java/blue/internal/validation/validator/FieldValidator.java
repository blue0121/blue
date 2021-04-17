package blue.internal.validation.validator;

import blue.core.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;


public abstract class FieldValidator
{
	protected String[] fields;
	protected final Map<Class<?>, Map<String, Method>> clazzMethodMap = new HashMap<>();
	
	public FieldValidator()
	{
	}
	
	protected Map<String, Method> init(Class<?> clazz)
	{
		Map<String, Method> methodMap = clazzMethodMap.get(clazz);
		if (methodMap != null)
			return methodMap;
		
		if (fields == null || fields.length == 0)
			return methodMap;
		
		synchronized (FieldNotBlankValidator.class)
		{
			methodMap = clazzMethodMap.get(clazz);
			if (methodMap != null)
				return methodMap;
			
			methodMap = new HashMap<>();
			clazzMethodMap.put(clazz, methodMap);
			
			List<Method> getterList = ReflectionUtil.getterList(clazz);
			
			Set<String> fieldSet = new HashSet<>();
			Stream.of(fields).forEach(field -> fieldSet.add(field));
			
			for (Method method : getterList)
			{
				String field = ReflectionUtil.field(method);
				if (fieldSet.contains(field))
					methodMap.put(field, method);
			}
			return methodMap;
		}
	}
	
}
