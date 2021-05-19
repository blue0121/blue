package blue.redis.internal.spring.config;

import blue.base.spring.common.SimpleBeanDefinitionParser;
import blue.redis.internal.spring.bean.RedisClientFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-13
 */
public class RedisClientParser extends SimpleBeanDefinitionParser {
	public RedisClientParser() {
		this.clazz = RedisClientFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "codec", "ref-codec");
		this.parse(element, builder, "mode", "mode");
		this.parse(element, builder, "broker", "broker");
		this.parse(element, builder, "masterName", "master-name");
		this.parse(element, builder, "database", "database");
		this.parse(element, builder, "password", "password");
		this.parse(element, builder, "timeoutMillis", "timeout-millis");
		this.parse(element, builder, "subscriptionConnectionPoolSize", "subscription-connection-pool-size");
		this.parse(element, builder, "connectionPoolSize", "connection-pool-size");
		this.parse(element, builder, "retry", "retry");
	}
}
