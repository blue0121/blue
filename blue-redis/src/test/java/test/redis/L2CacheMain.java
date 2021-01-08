package test.redis;

import blue.redis.L2Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-10
 */
public class L2CacheMain
{
	private static Logger logger = LoggerFactory.getLogger(L2CacheMain.class);

	public L2CacheMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/l2cache.xml");
		L2Cache cache = ctx.getBean("testKey", L2Cache.class);

		int n = 10;
		var nameList = getName(n);
		var valueList = getValue(n);

		cache.setSync(nameList, valueList);

		getSync(cache, nameList);
		getAsync(cache, nameList);

		cache.setSync("k1", "vv1");
		Thread.sleep(10);
		logger.info("key: k1, value: {}", (String)cache.getSync("k1"));

		cache.removeSync("k2");
		Thread.sleep(10);
		logger.info("key: k2, value: {}", (String)cache.getSync("k2"));

		cache.clearSync();
		getAsync(cache, nameList);

		Thread.sleep(5000);
		ctx.close();
	}

	private static List<String> getName(int n)
	{
		List<String> nameList = new ArrayList<>();
		for (int i = 0; i < n; i++)
		{
			nameList.add("k" + i);
		}
		return nameList;
	}

	private static  List<String> getValue(int n)
	{
		List<String> valueList = new ArrayList<>();
		for (int i = 0; i < n; i++)
		{
			valueList.add("v" + i);
		}
		return valueList;
	}

	private static void getSync(L2Cache cache, List<String> nameList)
	{
		logger.info("=============== getSync ===============");
		Map<String, String> map = cache.getSync(nameList.toArray(new String[0]));
		logger.info("map: {}", map);
		logger.info("=============== getSync ===============");
	}

	private static void getAsync(L2Cache cache, List<String> nameList)
	{
		logger.info("=============== getAsync ===============");
		cache.getAsync(map ->
		{
			logger.info("map: {}", map);
			logger.info("=============== getAsync ===============");
		}, nameList.toArray(new String[0]));
	}

}
