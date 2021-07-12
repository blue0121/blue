package blue.base.core.cache;

import blue.base.core.util.AssertUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class Singleton {
	private static final ConcurrentMap<Class<?>, Object> POOL = new ConcurrentHashMap<>();

	private Singleton() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, Object... params) {
		AssertUtil.notNull(clazz, "Class");
		return (T) POOL.computeIfAbsent(clazz, k -> newInstance(k, params));
	}

	private static <T> T newInstance(Class<T> clazz, Object... params) {
		try {
			return clazz.getConstructor().newInstance();
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, Function<Class<T>, T> f) {
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(f, "Function");
		return (T) POOL.computeIfAbsent(clazz, k -> f.apply(clazz));
	}

	public static void put(Object object) {
		AssertUtil.notNull(object, "Object");
		Object old = POOL.putIfAbsent(object.getClass(), object);
		if (old != null && old != object) {
			throw new IllegalArgumentException(object.getClass().getName() + " exist");
		}
	}

	public static void remove(Object object) {
		AssertUtil.notNull(object, "Object");
		if (object instanceof Class) {
			POOL.remove((Class<?>) object);
		}
		else {
			POOL.remove(object.getClass());
		}
	}

	public static void clear() {
		POOL.clear();
	}

	public static boolean isEmpty() {
		return POOL.isEmpty();
	}

	public static int size() {
		return POOL.size();
	}

}
