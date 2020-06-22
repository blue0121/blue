package blue.internal.core.convert;


import org.springframework.core.convert.converter.Converter;

import java.sql.Time;

/**
 * 字符串转化为SQL时间
 * 
 * @author zhengj
 * @since 1.0 2016年7月16日
 */
public class StringToSqlTimeConverter implements Converter<String, Time>, DateTimeConverter
{
	public StringToSqlTimeConverter()
	{
	}

	@Override
	public Time convert(String text)
	{
		if (text == null || text.isEmpty())
			return null;
		
		if (text.length() > TIME_LENGTH)
			text = text.substring(text.length() - TIME_LENGTH);
		
		return Time.valueOf(text);
	}
}
