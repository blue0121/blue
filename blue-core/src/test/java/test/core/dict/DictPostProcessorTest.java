package test.core.dict;

import blue.core.dict.DictParser;
import blue.core.dict.State;
import blue.core.dict.Type;
import blue.internal.core.dict.FastjsonEnumSerializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import test.core.model.Group;

/**
 * @author Jin Zheng
 * @since 2019-04-21
 */
@SpringJUnitConfig(locations = {"classpath:/spring/dict.xml"})
public class DictPostProcessorTest
{
	private static DictParser dictParser = DictParser.getInstance();

	public DictPostProcessorTest()
	{
	}

	@Test
	public void testState()
	{
		State normal = dictParser.getFromIndex(State.class, 0);
		Assertions.assertEquals(normal, State.NORMAL);
		State delete = dictParser.getFromIndex(State.class, 1);
		Assertions.assertEquals(delete, State.DELETE);

		Assertions.assertEquals(0, dictParser.getFromObject(State.NORMAL).intValue());
		Assertions.assertEquals(1, dictParser.getFromObject(State.DELETE).intValue());
	}

	@Test
	public void testTest()
	{
		test.core.dict.Test one = dictParser.getFromIndex(test.core.dict.Test.class, 1);
		Assertions.assertEquals(one, test.core.dict.Test.ONE);

		Assertions.assertEquals(1, dictParser.getFromObject(test.core.dict.Test.ONE).intValue());
	}

	@Test
	public void print()
	{
		SerializeConfig.getGlobalInstance().put(Type.class, new FastjsonEnumSerializer());
		SerializeConfig.getGlobalInstance().put(State.class, new FastjsonEnumSerializer());
		Group group = new Group(1, "blue", Type.YES, State.NORMAL);
		String json = JSON.toJSONString(group);
		System.out.println(json);
	}

}
