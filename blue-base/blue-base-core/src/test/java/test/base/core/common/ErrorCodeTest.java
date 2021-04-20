package test.base.core.common;

import blue.base.core.common.ErrorCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class ErrorCodeTest {
	public ErrorCodeTest() {
	}

	@Test
	public void test() {
		String msg = ErrorCode.ERROR.toJsonString();
		System.out.println(msg);
		JSONObject json = JSON.parseObject(msg);
		Assertions.assertEquals(500000, json.getIntValue("code"));
	}

}
