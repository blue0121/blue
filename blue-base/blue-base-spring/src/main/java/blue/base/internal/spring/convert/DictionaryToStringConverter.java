package blue.base.internal.spring.convert;

import blue.base.core.dict.DictParser;
import blue.base.core.dict.Dictionary;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Jin Zheng
 * @since 2020-04-13
 */
public class DictionaryToStringConverter implements Converter<Dictionary, String> {
	private DictParser dictParser = DictParser.getInstance();

	public DictionaryToStringConverter() {
	}

	@Override
	public String convert(Dictionary source) {
		return dictParser.getFieldFromObject(source);
	}
}
