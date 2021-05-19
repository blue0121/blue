package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisSequenceFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class RedisSequenceParser extends SimpleBeanDefinitionParser {
	public RedisSequenceParser() {
		this.clazz = RedisSequenceFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "redisClient", "ref-redis-client");
		this.parse(element, builder, "mode", "mode");
		this.parse(element, builder, "key", "key");
		this.parse(element, builder, "length", "length");
		this.parse(element, builder, "prefix", "prefix");
		this.parse(element, builder, "suffix", "suffix");
	}
}
