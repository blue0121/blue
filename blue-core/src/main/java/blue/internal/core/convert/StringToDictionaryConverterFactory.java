package blue.internal.core.convert;

import blue.core.dict.DictParser;
import blue.core.dict.Dictionary;
import blue.core.util.NumberUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StringToDictionaryConverterFactory implements ConverterFactory<String, Dictionary>
{
	private DictParser dictParser = DictParser.getInstance();
	
	@Override
	public <T extends Dictionary> Converter<String, T> getConverter(Class<T> targetType)
	{
		if (!Dictionary.class.isAssignableFrom(targetType))
			throw new IllegalArgumentException("目标类型不是字典类型：" + targetType.getName());
		
		return new StringToDictionary(targetType);
	}

	private class StringToDictionary<T extends Dictionary> implements Converter<String, T>
	{
		private final Class<T> dictType;

		public StringToDictionary(Class<T> dictType)
		{
			this.dictType = dictType;
		}

		@Override
		public T convert(String source)
		{
			if (source == null || source.isEmpty())
				return null;

			String str = source.trim();
			if (str.isEmpty())
				return null;

			if (NumberUtil.isInteger(str))
			{
				Integer index = Integer.valueOf(str);
				return dictParser.getFromIndex(dictType, index);
			}
			else
			{
				return dictParser.getFromField(dictType, str);
			}
		}
	}
}
