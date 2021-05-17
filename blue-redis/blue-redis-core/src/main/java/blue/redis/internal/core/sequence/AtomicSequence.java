package blue.redis.internal.core.sequence;

import blue.base.core.util.StringUtil;
import blue.redis.core.options.RedisSequenceOptions;
import org.redisson.api.RedissonClient;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class AtomicSequence extends AbstractSequence {
	public AtomicSequence(RedisSequenceOptions options, RedissonClient client) {
		super(options, client);
	}

	@Override
	protected void append(StringBuilder strVal, int len, long val) {
		strVal.append(StringUtil.leftPad(String.valueOf(val), len, ZERO));
	}

}
