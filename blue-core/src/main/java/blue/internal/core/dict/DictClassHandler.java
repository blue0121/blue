package blue.internal.core.dict;

import blue.core.dict.DictParser;
import blue.core.dict.Dictionary;
import blue.core.file.ClassHandler;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * 扫描字典配置
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public class DictClassHandler implements ClassHandler
{
	public DictClassHandler()
	{
	}

	@Override
	public void handle(Class<?> clazz)
	{
		if (Dictionary.class.isAssignableFrom(clazz))
		{
			DictParser.getInstance().parse(clazz);
			ParserConfig.getGlobalInstance().putDeserializer(clazz, new FastjsonEnumDeserializer(clazz));
			SerializeConfig.getGlobalInstance().put(clazz, new FastjsonEnumSerializer());
		}
		
	}
	
}
