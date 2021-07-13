package blue.base.core.reflect;

import blue.base.core.cache.Cache;
import blue.base.core.util.AssertUtil;
import blue.base.internal.core.reflect.DefaultJavaBean;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-21
 */
public class JavaBeanCache {
	private static Cache<Class<?>, JavaBean> cache;

	static {
		cache = Cache.builder()
				.maximumSize(10_000)
				.build();
	}

	private JavaBeanCache() {

	}

	public static JavaBean getJavaBean(Class<?> targetClass) {
		return getJavaBean(null, targetClass);
	}

	public static JavaBean getJavaBean(Object target, Class<?> targetClass) {
		AssertUtil.notNull(targetClass, "Target Class");
		JavaBean bean = cache.get(targetClass);
		if (bean == null) {
			synchronized (JavaBeanCache.class) {
				bean = cache.get(targetClass);
				if (bean == null) {
					bean = new DefaultJavaBean(target, targetClass);
					cache.put(targetClass, bean);
				}
			}
		}
		return bean;
	}

}
