package blue.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public class JsonUtil
{
	private static SerializerFeature[] serializer = new SerializerFeature[] {
			SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteClassName};

	private static SerializerFeature[] outstream = new SerializerFeature[] {
			SerializerFeature.WriteDateUseDateFormat};

	private static Feature[] feature = new Feature[] {};

	static
	{
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}

	private JsonUtil()
	{
	}

	public static byte[] toBytes(Object object)
	{
		if (object == null)
			return new byte[0];

		return JSON.toJSONBytes(object, serializer);
	}

	public static <T> T fromBytes(byte[] bytes)
	{
		if (bytes == null || bytes.length == 0)
			return null;

		return JSON.parseObject(bytes, Object.class, feature);
	}

	public static <T> T fromBytes(byte[] bytes, Class<T> clazz)
	{
		if (bytes == null || bytes.length == 0)
			return null;

		return JSON.parseObject(bytes, clazz, feature);
	}

	public static String output(Object object)
	{
		if (object == null)
			return null;

		return JSON.toJSONString(object, outstream);
	}

	public static String toString(Object object)
	{
		if (object == null)
			return null;

		return JSON.toJSONString(object, serializer);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromString(String json)
	{
		if (json == null || json.isEmpty())
			return null;

		return (T)JSON.parseObject(json, Object.class, feature);
	}

	public static <T> T fromString(String json, Class<T> clazz)
	{
		if (json == null || json.isEmpty())
			return null;

		return JSON.parseObject(json, clazz, feature);
	}

}
