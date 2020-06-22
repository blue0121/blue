package blue.internal.core.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 字符串转化为本地日期时间
 * 
 * @author zhengj
 * @since 1.0 2016年7月16日
 */
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime>, DateTimeConverter
{
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
	
	public StringToLocalDateTimeConverter()
	{
	}

	@Override
	public LocalDateTime convert(String text)
	{
		if (text == null || text.isEmpty())
			return null;

		if (text.indexOf(BLANK) == -1)
			text += " 00:00:00";

		return LocalDateTime.parse(text, dateTimeFormatter);
	}
}
