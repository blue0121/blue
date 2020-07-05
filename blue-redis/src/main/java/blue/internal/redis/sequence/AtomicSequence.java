package blue.internal.redis.sequence;

import blue.core.util.StringUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class AtomicSequence extends AbstractSequence
{
	public AtomicSequence()
	{
	}

	@Override
	protected void append(StringBuilder strVal, int len, long val)
	{
		strVal.append(StringUtil.leftPad(String.valueOf(val), len, "0"));
	}

}
