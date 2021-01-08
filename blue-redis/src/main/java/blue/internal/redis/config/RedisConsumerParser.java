package blue.internal.redis.config;

import blue.core.common.ListenerContainerParser;
import blue.internal.core.message.ConsumerListenerConfig;
import blue.internal.redis.consumer.DefaultRedisConsumer;
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
public class RedisConsumerParser extends ListenerContainerParser
{
	public RedisConsumerParser()
	{
		this.clazz = DefaultRedisConsumer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		NodeList configList = element.getElementsByTagNameNS(RedisNamespaceHandler.NS, "listener");
		if (configList.getLength() > 0)
		{
			this.parseListenerConfig(configList, builder.getRawBeanDefinition(), ConsumerListenerConfig.class);
		}

		this.parse(element, builder, "name", "id");
		this.parseRef(element, builder, "redisson", "ref-redisson");
		this.parseRef(element, builder, "exceptionHandler", "ref-exception-handler");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
	}

}
