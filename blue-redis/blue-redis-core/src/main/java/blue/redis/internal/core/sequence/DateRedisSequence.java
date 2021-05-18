package blue.redis.internal.core.sequence;

import blue.base.core.util.StringUtil;
import blue.redis.core.options.RedisSequenceOptions;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class DateRedisSequence extends AbstractRedisSequence {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public DateRedisSequence(RedisSequenceOptions options, RedissonClient client) {
		super(options, client);
	}

	@Override
	protected void append(StringBuilder strVal, int len, long val) {
		strVal.append(LocalDateTime.now().format(formatter));
		strVal.append(StringUtil.leftPad(String.valueOf(val), len - 8, ZERO));
	}

}
