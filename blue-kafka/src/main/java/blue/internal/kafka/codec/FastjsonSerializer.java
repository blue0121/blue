package blue.internal.kafka.codec;

import blue.core.util.JsonUtil;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Fastjson 序列化
 *
 * @author Jin Zheng
 * @since 1.0 2019-02-15
 */
public class FastjsonSerializer implements Serializer<Object>
{
	public FastjsonSerializer()
	{
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey)
	{
	}

	@Override
	public byte[] serialize(String topic, Object data)
	{
		return JsonUtil.toBytes(data);
	}

	@Override
	public void close()
	{

	}
}
