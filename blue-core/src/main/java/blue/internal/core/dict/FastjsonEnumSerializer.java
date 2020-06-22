package blue.internal.core.dict;

import blue.core.dict.Dictionary;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author zhengjin
 * @since 1.0 2017年12月08日
 */
public class FastjsonEnumSerializer implements ObjectSerializer
{
	public FastjsonEnumSerializer()
	{
	}

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException
	{
		SerializeWriter out = serializer.out;

		if (object instanceof Dictionary)
		{
			Dictionary dict = (Dictionary) object;
			out.writeInt(dict.getIndex());
		}

	}
}
