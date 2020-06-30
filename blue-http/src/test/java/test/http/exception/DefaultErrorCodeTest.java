package test.http.exception;

import blue.http.exception.DefaultErrorCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class DefaultErrorCodeTest
{
	public DefaultErrorCodeTest()
	{
	}

	@Test
	public void test()
	{
		String msg = DefaultErrorCode.ERROR.toJsonString();
		System.out.println(msg);
		JSONObject json = JSON.parseObject(msg);
		Assertions.assertEquals(500000, json.getIntValue("code"));
	}

}
