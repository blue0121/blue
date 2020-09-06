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

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Class<?> clazz)
	{
		if (Dictionary.class.isAssignableFrom(clazz))
		{
			Class<Dictionary> dictClazz = (Class<Dictionary>)clazz;
			DictParser.getInstance().parse(dictClazz);
			ParserConfig.getGlobalInstance().putDeserializer(clazz, new FastjsonEnumDeserializer(dictClazz));
			SerializeConfig.getGlobalInstance().put(clazz, new FastjsonEnumSerializer());
		}
		
	}
	
}
