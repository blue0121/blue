package test.redis.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.redis.spring.model.User;
import test.redis.spring.service.UserService;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-14
 */
public class CacheMain {
	public CacheMain() {
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/cache.xml");
		UserService service = ctx.getBean(UserService.class);

		service.testLock();

		save(service);
		get(service);
		remove(service);
		get(service);
		Thread.sleep(600);
		get(service);
		Thread.sleep(1300);
		get(service);

		ctx.close();
	}

	private static void save(UserService service) {
		for (int i = 0; i < 5; i++) {
			service.save(new User(i, "test_" + i));
		}
	}

	private static void get(UserService service) {
		System.out.println("=============== get ===============");
		for (int i = 0; i < 5; i++) {
			User user = service.get(i);
			if (user == null) {
				System.out.printf("id: %d, name: null\n", i);
			}
			else {
				System.out.printf("id: %d, name: %s\n", user.getId(), user.getName());
			}
		}
		System.out.println("=============== get ===============");
	}

	private static void remove(UserService service) {
		service.remove(1);
		service.remove(4);
	}

}
