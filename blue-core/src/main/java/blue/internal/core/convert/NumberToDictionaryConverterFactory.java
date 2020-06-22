package blue.internal.core.convert;

import blue.core.dict.DictParser;
import blue.core.dict.Dictionary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class NumberToDictionaryConverterFactory implements ConverterFactory<Number, Dictionary>
{
	private DictParser dictParser = DictParser.getInstance();
	
	@Override
	public <T extends Dictionary> Converter<Number, T> getConverter(Class<T> targetType)
	{
		if (!Dictionary.class.isAssignableFrom(targetType))
			throw new IllegalArgumentException("目标类型不是字典类型：" + targetType.getName());
		
		return new NumberToDictionary(targetType);
	}

	private class NumberToDictionary<T extends Dictionary> implements Converter<Number, T>
	{
		private final Class<T> dictType;

		public NumberToDictionary(Class<T> dictType)
		{
			this.dictType = dictType;
		}

		@Override
		public T convert(Number source)
		{
			return dictParser.getFromIndex(dictType, source.intValue());
		}
	}
}
