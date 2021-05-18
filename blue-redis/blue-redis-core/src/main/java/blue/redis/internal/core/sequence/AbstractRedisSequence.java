package blue.redis.internal.core.sequence;

import blue.redis.core.RedisSequence;
import blue.redis.core.options.RedisSequenceOptions;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public abstract class AbstractRedisSequence implements RedisSequence {
	private static Logger logger = LoggerFactory.getLogger(AbstractRedisSequence.class);
	public static final String ZERO = "0";

	protected final RAtomicLong counter;
	protected final RedisSequenceOptions options;

	public AbstractRedisSequence(RedisSequenceOptions options, RedissonClient client) {
		this.options = options;
		this.options.check();
		this.counter = client.getAtomicLong(options.getKey());
	}

	@Override
	public final String nextValue() {
		StringBuilder strVal = new StringBuilder(options.getLength()).append(options.getPrefix());

		long val = counter.incrementAndGet();
		logger.debug("Sequence '{}' current counter: {}, key: {}", options.getId(), val, options.getKey());

		this.append(strVal, options.valueLength(), val);

		strVal.append(options.getSuffix());
		String nextValue = strVal.toString();
		logger.debug("Sequence '{}' next value: {}", options.getId(), nextValue);
		return nextValue;
	}

	@Override
	public final void reset() {
		counter.setAsync(0L);
		logger.info("Sequence '{}' reset, mode: {}, key: {}", options.getId(), options.getMode(), options.getKey());
	}

	protected abstract void append(StringBuilder strVal, int len, long val);
}
