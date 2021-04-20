package test.base.core.util;

import blue.base.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtilTest
{
	public StringUtilTest()
	{
	}
	
	@Test
	public void testTemplate1()
	{
		String tpl = "{{first.DATA}}车牌：{{keyword1.DATA}}投保公司：{{keyword2.DATA}}车主：{{keyword3.DATA}}报价金额：{{keyword4.DATA}}时间：{{keyword5.DATA}}{{remark.DATA}}";
		String result = "你好车牌：粤A12345投保公司：中国平安车主：羊驼报价金额：￥1000元时间：2016-02-14通知";
		Map<String, String> param = new HashMap<>();
		param.put("first", "你好");
		param.put("keyword1", "粤A12345");
		param.put("keyword2", "中国平安");
		param.put("keyword3", "羊驼");
		param.put("keyword4", "￥1000元");
		param.put("keyword5", "2016-02-14");
		param.put("remark", "通知");
		String content = StringUtil.template(tpl, param, "{{", ".DATA}}");
		System.out.println(content);
		Assertions.assertEquals(result, content, "模板消息错误");
	}
	
	@Test
	public void testTemplate2()
	{
		String tpl = "{one}-{two}={one}";
		String result = "2-1=2";
		Map<String, String> param = new HashMap<>();
		param.put("one", "2");
		param.put("two", "1");
		String content = StringUtil.template(tpl, param, "{", "}");
		System.out.println(content);
		Assertions.assertEquals(result, content, "模板消息错误");
	}

	@Test
	public void testSplit()
	{
		String s1 = "1,2;3   4  5|6";
		List<String> list1 = Arrays.asList("1", "2", "3", "4", "5", "6");
		Assertions.assertEquals(list1, StringUtil.split(s1));
		Assertions.assertTrue(StringUtil.split(null).isEmpty());
		Assertions.assertTrue(StringUtil.split("").isEmpty());
	}

	@Test
	public void testLeftPad()
	{
		Assertions.assertNull(StringUtil.leftPad(null, 0, "0"));
		Assertions.assertEquals("000123", StringUtil.leftPad("123", 6, "0"));
		Assertions.assertEquals("1234", StringUtil.leftPad("1234", 2, "0"));
		Assertions.assertEquals("01234", StringUtil.leftPad("1234", 5, "0"));
		Assertions.assertEquals("001234", StringUtil.leftPad("1234", 5, "00"));
	}

	@Test
	public void testJoin()
	{
		Assertions.assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c"), ","));
		Assertions.assertEquals("a", StringUtil.join(Arrays.asList("a"), ","));
		Assertions.assertEquals("abc", StringUtil.join(Arrays.asList("a", "b", "c"), ""));
		Assertions.assertEquals("abc", StringUtil.join(Arrays.asList("a", "b", "c"), null));
		Assertions.assertNull(StringUtil.join(null, null));
		Assertions.assertNull(StringUtil.join(new ArrayList<>(), null));
	}

	@Test
	public void testGetJdbcType()
	{
		Assertions.assertEquals("mysql", StringUtil.getJdbcType("jdbc:mysql://localhost:3306/db"));
		Assertions.assertEquals("oracle", StringUtil.getJdbcType("jdbc:oracle:thin:@localhost:1521:db"));
		Assertions.assertEquals("sqlserver", StringUtil.getJdbcType("jdbc:sqlserver://localhost:1433;databasename=db"));
		Assertions.assertEquals("postgresql", StringUtil.getJdbcType("jdbc:postgresql://localhost:5432/db"));
	}

	@Test
	public void testGetString()
	{
		String json = StringUtil.getString(StringUtilTest.class, "/json/string.json");
		System.out.println(json);
		Assertions.assertNotNull(json);
		Assertions.assertFalse(json.isEmpty());
		JSONObject object = JSON.parseObject(json);
		Assertions.assertNotNull(object);
		Assertions.assertEquals("blue", object.getString("name"));
	}
	
}
