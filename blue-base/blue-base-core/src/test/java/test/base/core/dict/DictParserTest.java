package test.base.core.dict;

import blue.base.core.dict.DictParser;
import blue.base.core.dict.DictValue;
import blue.base.core.dict.State;
import blue.base.core.dict.Type;
import blue.base.internal.core.dict.FastjsonEnumSerializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.base.core.model.Group;

import java.util.List;
import java.util.Map;

public class DictParserTest
{
	private static DictParser dictParser;
	
	public DictParserTest()
	{
	}
	
	@BeforeAll
	public static void beforeClass()
	{
		dictParser = DictParser.getInstance();
		dictParser.parse(State.class);
		dictParser.parse(Type.class);
	}
	
	@Test
	public void testState()
	{
		Assertions.assertEquals(State.NORMAL, dictParser.getFromIndex(State.class, 0));
		Assertions.assertEquals(State.DELETE, dictParser.getFromIndex(State.class, 1));
		Assertions.assertEquals(0, dictParser.getFromObject(State.NORMAL).intValue());
		Assertions.assertEquals(1, dictParser.getFromObject(State.DELETE).intValue());
		Assertions.assertNull(dictParser.getFromIndex(State.class, -1));
	}

	@Test
	public void testGetDictValue()
	{
		List<DictValue> valueList = dictParser.getDictValue(State.class);
		Assertions.assertNotNull(valueList);
		Assertions.assertEquals(2, valueList.size());
		DictValue normalValue = valueList.get(0);
		DictValue deleteValue = valueList.get(1);
		Assertions.assertEquals(0, normalValue.getValue().intValue());
		Assertions.assertEquals(1, deleteValue.getValue().intValue());
	}
	
	@Test
	public void testStringMap()
	{
		Map<String, Map<String, String>> stringMap = dictParser.getStringMap();
		System.out.println(stringMap);
		Assertions.assertTrue(stringMap.size() >= 2);
	}

	@Test
	public void print() {
		SerializeConfig.getGlobalInstance().put(Type.class, new FastjsonEnumSerializer());
		SerializeConfig.getGlobalInstance().put(State.class, new FastjsonEnumSerializer());
		Group group = new Group(1, "blue", Type.YES, State.NORMAL);
		String json = JSON.toJSONString(group);
		System.out.println(json);
	}
	
}
