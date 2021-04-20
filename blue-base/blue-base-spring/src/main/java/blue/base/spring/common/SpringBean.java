package blue.base.spring.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Get bean from spring IoC
 *
 * @author Jin Zheng
 * @since 2020-02-21
 */
public class SpringBean implements ApplicationContextAware {
	private static ApplicationContext ctx;

	public SpringBean() {
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return ctx.getBean(name, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		SpringBean.ctx = ctx;
	}
}
