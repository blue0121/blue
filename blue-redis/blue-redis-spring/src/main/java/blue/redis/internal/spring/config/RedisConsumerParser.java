package blue.redis.internal.spring.config;

import blue.base.spring.common.ListenerContainerParser;
import blue.redis.internal.spring.bean.RedisConsumerConfig;
import blue.redis.internal.spring.bean.RedisConsumerFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Redis Listener Container 解析
 *
 * @author Jin Zheng
 * @since 1.0 2019-04-10
 */
public class RedisConsumerParser extends ListenerContainerParser {
	public RedisConsumerParser() {
		this.clazz = RedisConsumerFactoryBean.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NodeList configList = element.getElementsByTagNameNS(RedisNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0) {
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), RedisConsumerConfig.class);
		}

		this.parse(element, builder, "id", "id");
		this.parseRef(element, builder, "redisClient", "ref-redis-client");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "executor", "ref-executor");
	}

}
