package test.redis.service;

import blue.redis.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import test.redis.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-14
 */
public class UserService
{
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	private Map<Integer, User> userMap = new HashMap<>();

	public UserService()
	{
	}

	@Cacheable(value = "user", key = "#id")
	public User get(Integer id)
	{
		User user = userMap.get(id);
		if (user == null)
		{
			logger.info("get user is null");
		}
		else
		{
			logger.info("get user, id: {}, name: {}", user.getId(), user.getName());
		}
		return user;
	}

	@CachePut(value = "user", key = "#user.id")
	public User save(User user)
	{
		userMap.put(user.getId(), user);
		logger.info("save user, id: {}, name: {}", user.getId(), user.getName());
		return user;
	}

	@CacheEvict(value = "user", key = "#id")
	public void remove(Integer id)
	{
		userMap.remove(id);
		logger.info("remove user, id: {}", id);
	}

	@RedisLock("user~lock")
	public void testLock()
	{
		logger.info(">>>> Redis lock test");
	}

}
