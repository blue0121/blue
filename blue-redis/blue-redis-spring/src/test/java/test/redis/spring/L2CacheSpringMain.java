package test.redis.spring;

import blue.redis.core.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-10
 */
public class L2CacheSpringMain {
	private static Logger logger = LoggerFactory.getLogger(L2CacheSpringMain.class);

	public L2CacheSpringMain() {
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/l2cache.xml");
		RedisCache<String> cache = ctx.getBean("testKey", RedisCache.class);

		int n = 10;
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < n; i++) {
			map.put("k" + i, "v" + i);
		}

		cache.setMap(map);

		getSync(cache, map.keySet());

		cache.set("k1", "vv1");
		Thread.sleep(10);
		logger.info("key: k1, value: {}", cache.get("k1"));

		cache.remove("k2");
		Thread.sleep(10);
		logger.info("key: k2, value: {}", cache.get("k2"));

		cache.clear();
		getSync(cache, map.keySet());

		ctx.close();
	}

	private static void getSync(RedisCache<String> cache, Collection<String> nameList) {
		logger.info("=============== getSync ===============");
		Map<String, String> map = cache.getMap(nameList.toArray(new String[0]));
		logger.info("map: {}", map);
		logger.info("=============== getSync ===============");
	}

}
