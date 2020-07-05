package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.codec.FastjsonCodec;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class FastjsonCodecParser extends SimpleBeanDefinitionParser
{
	public FastjsonCodecParser()
	{
		this.clazz = FastjsonCodec.class;
	}

}
