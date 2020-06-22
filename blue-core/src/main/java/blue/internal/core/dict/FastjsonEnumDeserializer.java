package blue.internal.core.dict;

import blue.core.dict.DictParser;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * @author zhengjin
 * @since 1.0 2017年12月08日
 */
public class FastjsonEnumDeserializer implements ObjectDeserializer
{
	private DictParser dictParser = DictParser.getInstance();
	private Class<?> clazz;

	public FastjsonEnumDeserializer(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		int token = parser.lexer.token();
		if (token == JSONToken.LITERAL_INT)
		{
			Integer intValue = parser.parseObject(int.class);
			return (T) dictParser.getFromIndex(clazz, intValue);
		}
		else if (token == JSONToken.LITERAL_STRING)
		{
			String strValue = parser.parseObject(String.class);
			return (T) dictParser.getFromField(clazz, strValue);
		}
		return null;
	}

	@Override
	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_INT;
	}
}
