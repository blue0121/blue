package blue.internal.core.dict;

import blue.core.dict.DictParser;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Fastjson 自定义序列化接口
 *
 * @author zhengjin
 * @since 1.0 2017年12月07日
 */
public class FastjsonEnumValueFilter implements ValueFilter
{
	private DictParser dictParser = DictParser.getInstance();

	public FastjsonEnumValueFilter()
	{
	}

	@Override
	public Object process(Object object, String name, Object value)
	{
		if (value == null)
			return value;

		if (value instanceof Enum)
		{
			Enum<?> enumObject = (Enum<?>)value;
			Integer index = dictParser.getFromObject(value);
			if (index == null)
			{
				index = enumObject.ordinal();
			}
			return index;
		}

		return value;
	}

}
