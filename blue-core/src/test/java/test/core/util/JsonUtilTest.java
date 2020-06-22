package test.core.util;

import blue.core.dict.State;
import blue.core.dict.Type;
import blue.core.util.JsonUtil;
import blue.internal.core.dict.FastjsonEnumDeserializer;
import blue.internal.core.dict.FastjsonEnumSerializer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.junit.jupiter.api.Test;
import test.core.model.Group;

/**
 * @author zhengjin
 * @since 1.0 2017年12月07日
 */
public class JsonUtilTest
{
	public JsonUtilTest()
	{
	}

	@Test
	public void testOutput()
	{
		Group group = new Group(1, "blue", Type.YES, State.NORMAL);
		String json = JsonUtil.output(group);
		System.out.println(json);

		Group g2 = new Group();
		g2.setId(2);
		g2.setName("blue");
		String json2 = JsonUtil.output(g2);
		System.out.println(json2);
	}

	@Test
	public void testDecode()
	{
		ParserConfig.getGlobalInstance().putDeserializer(Type.class, new FastjsonEnumDeserializer(Type.class));
		ParserConfig.getGlobalInstance().putDeserializer(State.class, new FastjsonEnumDeserializer(State.class));
		SerializeConfig.getGlobalInstance().put(Type.class, new FastjsonEnumSerializer());
		SerializeConfig.getGlobalInstance().put(State.class, new FastjsonEnumSerializer());

		Group group = new Group(1, "blue", Type.NO, State.NORMAL);
		String json = JsonUtil.output(group);
		System.out.println(json);

		Group g2 = JsonUtil.fromString(json, Group.class);
		System.out.println(JsonUtil.toString(g2));

		String json2 = "{\"id\":1,\"name\":\"blue\",\"type\":\"YES\",\"state\":\"DELETE\"}";
		Group g3 = JsonUtil.fromString(json2, Group.class);
		System.out.println(JsonUtil.toString(g3));
	}

}

