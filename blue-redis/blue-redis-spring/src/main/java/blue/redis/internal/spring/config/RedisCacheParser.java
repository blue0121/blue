package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisCacheFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-10
 */
public class RedisCacheParser extends SimpleBeanDefinitionParser {
	public RedisCacheParser() {
		this.clazz = RedisCacheFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "redisClient", "ref-redis-client");
		this.parse(element, builder, "mode", "mode");
		this.parse(element, builder, "prefix", "prefix");
		this.parse(element, builder, "ttl", "ttl");
		this.parse(element, builder, "localTtl", "local-ttl");
		this.parse(element, builder, "localMaxSize", "local-max-size");
		this.parse(element, builder, "timeout", "timeout");
		this.parse(element, builder, "retry", "retry");
		this.parse(element, builder, "writeMode", "write-mode");
		this.parseRef(element, builder, "loader", "ref-loader");
		this.parseRef(element, builder, "writer", "ref-writer");
	}
}
