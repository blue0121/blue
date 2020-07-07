package blue.internal.kafka.codec;

import blue.core.util.JsonUtil;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-02-15
 */
public class FastjsonDeserializer implements Deserializer<Object>
{
	public FastjsonDeserializer()
	{
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey)
	{
	}

	@Override
	public Object deserialize(String topic, byte[] data)
	{
		return JsonUtil.fromBytes(data);
	}

	@Override
	public void close()
	{
	}
}
