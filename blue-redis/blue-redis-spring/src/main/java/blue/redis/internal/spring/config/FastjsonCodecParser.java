package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.core.codec.FastjsonCodec;

/**
 * @author Jin Zheng
 * @since 2019-11-03
 */
public class FastjsonCodecParser extends SimpleBeanDefinitionParser {
	public FastjsonCodecParser() {
		this.clazz = FastjsonCodec.class;
	}

}
