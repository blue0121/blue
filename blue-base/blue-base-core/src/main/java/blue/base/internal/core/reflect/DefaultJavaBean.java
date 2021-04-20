package blue.base.internal.core.reflect;

import blue.base.core.collection.MultiMap;
import blue.base.core.reflect.BeanConstructor;
import blue.base.core.reflect.BeanField;
import blue.base.core.reflect.BeanMethod;
import blue.base.core.reflect.JavaBean;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-24
 */
public class DefaultJavaBean extends DefaultAnnotationOperation implements JavaBean {
	private static Logger logger = LoggerFactory.getLogger(DefaultJavaBean.class);
	private static final Set<String> METHOD_SET = Set.of("wait", "equals", "toString", "hashCode", "getClass",
			"notify", "notifyAll");

	private final Object target;
	private final Class<?> targetClass;

	private List<Class<?>> superClassList;
	private List<Class<?>> interfaceList;

	private Map<ParamClassKey, BeanConstructor> constructorMap;
	private List<BeanConstructor> constructorList;
	private final Cache<ParamClassKey, BeanConstructor> constructorCache;

	private Map<String, BeanMethod> getterMap;
	private MultiMap<String, BeanMethod> setterMap;
	private List<BeanMethod> allMethodList;
	private List<BeanMethod> otherMethodList;

	private Map<String, BeanField> fieldMap;

	public DefaultJavaBean(Class<?> targetClass) {
		this(null, targetClass);
	}

	public DefaultJavaBean(Object target, Class<?> targetClass) {
		super(targetClass);
		this.target = target;
		this.targetClass = targetClass;
		this.parseClass();
		if (logger.isDebugEnabled()) {
			logger.debug("class: {}, super classes: {}, interfaces: {}, annotations: {}",
					this.getName(), superClassList, interfaceList, this.getAnnotations());
		}
		this.parseConstructor();
		this.parseMethod();
		this.parseField();
		this.constructorCache = Caffeine.newBuilder()
				.expireAfterAccess(Duration.ofHours(1))
				.maximumSize(1_000)
				.build();
	}

	@Override
	public String getName() {
		return targetClass.getSimpleName();
	}

	@Override
	public Class<?> getTargetClass() {
		return targetClass;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public List<BeanConstructor> getAllConstructors() {
		return constructorList;
	}

	@Override
	public BeanConstructor getConstructor(Class<?>... classes) {
		ParamClassKey key = new ParamClassKey(classes);
		BeanConstructor constructor = constructorMap.get(key);
		if (constructor != null) {
			return constructor;
		}

		constructor = constructorCache.getIfPresent(key);
		if (constructor != null) {
			return constructor;
		}

		try {
			Constructor<?> cons = targetClass.getConstructor(classes);
			constructor = new DefaultBeanConstructor(cons, superClassList);
			constructorCache.put(key, constructor);
		}
		catch (NoSuchMethodException e) {
			logger.warn("There is no constructor, class: {}, param classes: {}",
					targetClass.getName(), Arrays.toString(classes));
		}
		return constructor;
	}

	@Override
	public Object newInstance(Object... params) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?>[] classes = this.getParamClasses(params);
		BeanConstructor constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstance(params);
	}

	@Override
	public Object newInstanceQuietly(Object... params) {
		Class<?>[] classes = this.getParamClasses(params);
		BeanConstructor constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstanceQuietly(params);
	}

	@Override
	public List<BeanMethod> getAllMethods() {
		return allMethodList;
	}

	@Override
	public Map<String, BeanField> getAllFields() {
		return fieldMap;
	}

	@Override
	public BeanField getField(String fieldName) {
		return fieldMap.get(fieldName);
	}

	private void parseClass() {
		List<Class<?>> superList = new ArrayList<>();
		List<Class<?>> interList = new ArrayList<>();
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		this.parseSuperClass(targetClass, superList, interList, annotationMap);
		for (var cls : superList) {
			for (var inter : cls.getInterfaces()) {
				this.parseSuperClass(inter, superList, interList, annotationMap);
			}
		}
		this.superClassList = List.copyOf(superList);
		this.interfaceList = List.copyOf(interList);
		this.initAnnotationMap(annotationMap);
	}

	private void parseSuperClass(Class<?> cls, List<Class<?>> superList, List<Class<?>> interList,
	                             Map<Class<?>, Annotation> annotationMap) {
		Queue<Class<?>> queue = new LinkedList<>();
		queue.offer(cls);
		Class<?> clazz = null;
		while ((clazz = queue.poll()) != null) {
			if (clazz == Object.class) {
				continue;
			}

			if (clazz.isInterface()) {
				interList.add(clazz);
			}
			else {
				superList.add(clazz);
			}
			this.parseAnnotation(clazz, annotationMap);
			queue.offer(clazz.getSuperclass());
		}
	}

	private void parseAnnotation(Class<?> clazz, Map<Class<?>, Annotation> annotationMap) {
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		for (var annotation : annotations) {
			if (annotationMap.containsKey(annotation.annotationType())) {
				continue;
			}

			annotationMap.put(annotation.annotationType(), annotation);
		}
	}

	private void parseConstructor() {
		Map<ParamClassKey, BeanConstructor> consMap = new HashMap<>();
		Constructor<?>[] constructors = targetClass.getConstructors();
		for (var constructor : constructors) {
			BeanConstructor beanConstructor = new DefaultBeanConstructor(constructor, superClassList);
			ParamClassKey key = new ParamClassKey(constructor.getParameterTypes());
			consMap.put(key, beanConstructor);
		}
		this.constructorMap = Map.copyOf(consMap);
		this.constructorList = List.copyOf(consMap.values());
	}

	private void parseMethod() {
		Map<String, BeanMethod> getter = new HashMap<>();
		MultiMap<String, BeanMethod> setter = MultiMap.create();
		List<BeanMethod> all = new ArrayList<>();
		List<BeanMethod> other = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (var method : methods) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) || METHOD_SET.contains(method.getName())) {
				continue;
			}

			BeanMethod beanMethod = new DefaultBeanMethod(target, method, superClassList, interfaceList);
			all.add(beanMethod);
			if (beanMethod.isGetter()) {
				getter.put(beanMethod.getRepresentField(), beanMethod);
			}
			else if (beanMethod.isSetter()) {
				setter.put(beanMethod.getRepresentField(), beanMethod);
			}
			else {
				other.add(beanMethod);
			}
		}
		this.getterMap = Map.copyOf(getter);
		this.setterMap = MultiMap.copyOf(setter);
		this.allMethodList = List.copyOf(all);
		this.otherMethodList = List.copyOf(other);
	}

	private void parseField() {
		Map<String, BeanField> beanFieldMap = new HashMap<>();
		Map<String, Field> map = new HashMap<>();
		this.filterField(this.targetClass.getDeclaredFields(), map);
		this.filterField(this.targetClass.getFields(), map);
		for (var entry : map.entrySet()) {
			BeanMethod getter = getterMap.get(entry.getKey());
			Set<BeanMethod> setterSet = setterMap.get(entry.getKey());
			BeanMethod setter = this.getSetterMethod(getter, setterSet);
			BeanField field = new DefaultBeanField(entry.getKey(), target, entry.getValue(), getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : getterMap.entrySet()) {
			if (beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			Set<BeanMethod> setterSet = setterMap.get(entry.getKey());
			BeanMethod setter = this.getSetterMethod(entry.getValue(), setterSet);
			BeanField field = new DefaultBeanField(entry.getKey(), target, null, entry.getValue(), setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : setterMap.entrySet()) {
			if (entry.getValue().size() != 1 || beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			BeanMethod setter = entry.getValue().iterator().next();
			BeanMethod getter = getterMap.get(entry.getKey());
			BeanField field = new DefaultBeanField(entry.getKey(), target, null, getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}

		this.fieldMap = Map.copyOf(beanFieldMap);
	}

	private BeanMethod getSetterMethod(BeanMethod getter, Set<BeanMethod> setterSet) {
		if (setterSet == null || setterSet.isEmpty()) {
			return null;
		}

		if (getter == null) {
			if (setterSet.size() != 1) {
				return null;
			}

			return setterSet.iterator().next();
		}
		Class<?> returnClass = getter.getReturnClass();
		for (var setter : setterSet) {
			if (setter.getParamClassList() == null || setter.getParamClassList().size() != 1) {
				continue;
			}

			if (returnClass == setter.getParamClassList().get(0)) {
				return setter;
			}
		}
		return null;
	}

	private void filterField(Field[] fields, Map<String, Field> map) {
		for (var field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
				continue;
			}
			if (map.containsKey(field.getName())) {
				continue;
			}

			map.put(field.getName(), field);
		}
	}

	private Class<?>[] getParamClasses(Object... params) {
		if (params.length == 0) {
			return new Class<?>[0];
		}

		Class<?>[] classes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			classes[i] = params[i].getClass();
		}
		return classes;
	}

}
