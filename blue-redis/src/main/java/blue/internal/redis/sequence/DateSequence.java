package blue.internal.redis.sequence;

import blue.core.util.StringUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class DateSequence extends AbstractSequence
{
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public DateSequence()
	{
	}

	@Override
	protected void append(StringBuilder strVal, int len, long val)
	{
		strVal.append(LocalDateTime.now().format(formatter));
		strVal.append(StringUtil.leftPad(String.valueOf(val), len-8, "0"));
	}

}