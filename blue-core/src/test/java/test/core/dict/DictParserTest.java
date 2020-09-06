package test.core.dict;

import blue.core.dict.DictParser;
import blue.core.dict.DictValue;
import blue.core.dict.State;
import blue.core.dict.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
	
	
}
