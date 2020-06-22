package test.core.util;

import blue.core.dict.State;
import blue.core.util.BeanUtil;
import blue.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.core.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjin
 * @since 1.0 2017年11月29日
 */
public class BeanUtilTest
{
	public BeanUtilTest()
	{
	}

	@Test
	public void testCreateBean()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("id", 12);
		map.put("name", "blue");
		map.put("score", "100.0");
		map.put("birthday", DateUtil.parseDate("2000-01-01"));
		map.put("loginTime", DateUtil.parseDateTime("2000-01-01 00:00:00"));
		map.put("state", State.NORMAL.getIndex());
		User user = BeanUtil.createBean(User.class, map);
		Assertions.assertNotNull(user);
		Assertions.assertEquals(12, user.getId().intValue());
		Assertions.assertEquals("blue", user.getName());
		Assertions.assertEquals(100.0, user.getScore().doubleValue(), 0.01);
		Assertions.assertEquals("2000-01-01", user.getBirthday().toString());
		Assertions.assertEquals("2000-01-01 00:00:00", DateUtil.formatDateTime(user.getLoginTime()));
		Assertions.assertNotNull(user.getState(), "状态不能为空");
		Assertions.assertEquals(State.NORMAL, user.getState(), "状态错误");
	}

}

