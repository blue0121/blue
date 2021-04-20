package blue.base.internal.spring.convert;

import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

/**
 * 字符串转化为SQL时间戳
 *
 * @author zhengj
 * @since 1.0 2016年7月16日
 */
public class StringToSqlTimestampConverter implements Converter<String, Timestamp>, DateTimeConverter {
	public StringToSqlTimestampConverter() {
	}

	@Override
	public Timestamp convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (text.indexOf(BLANK) == -1) {
			text += " 00:00:00";
		}

		return Timestamp.valueOf(text);
	}
}
