package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisProducerFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Redis Producer 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class RedisProducerParser extends SimpleBeanDefinitionParser {
	public RedisProducerParser() {
		this.clazz = RedisProducerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "redisClient", "ref-redis-client");
		this.parseRef(element, builder, "producerListener", "ref-producer-listener");
	}
}
