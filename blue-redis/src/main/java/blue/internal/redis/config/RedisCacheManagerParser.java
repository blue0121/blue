package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.cache.RedisCacheConfig;
import blue.internal.redis.cache.RedisCacheManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jin Zheng
 * @since 1.0 2020-12-14
 */
public class RedisCacheManagerParser extends SimpleBeanDefinitionParser
{
	public RedisCacheManagerParser()
	{
		this.clazz = RedisCacheManager.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		NodeList configList = element.getElementsByTagNameNS(RedisNamespaceHandler.NS, "cache-config");
		this.parseCacheConfig(configList, builder.getRawBeanDefinition());

		this.parseRef(element, builder, "redisson", "ref-redisson");
		this.parse(element, builder, "prefix", "prefix");
		this.parse(element, builder, "defaultTtl", "default-ttl");
		this.parse(element, builder, "defaultLocalTtl", "default-local-ttl");
		this.parse(element, builder, "defaultLocalMaxSize", "default-local-max-size");
		this.parse(element, builder, "defaultTimeout", "default-timeout");
	}

	protected final void parseCacheConfig(NodeList nodeList, BeanDefinition beanDefinition)
	{
		if (nodeList == null || nodeList.getLength() == 0)
			return;

		ManagedList<BeanDefinitionHolder> managedList = new ManagedList<>();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node instanceof Element)
			{
				Element element = (Element) node;
				RootBeanDefinition definition = new RootBeanDefinition();
				definition.setBeanClass(RedisCacheConfig.class);
				this.parse(element, definition, "name", "name");
				this.parse(element, definition, "ttl", "ttl");
				this.parse(element, definition, "localTtl", "local-ttl");
				this.parse(element, definition, "localMaxSize", "local-max-size");
				this.parse(element, definition, "timeout", "timeout");

				BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, clazz.getName());
				managedList.add(holder);
			}
		}
		if (!managedList.isEmpty())
		{
			beanDefinition.getPropertyValues().addPropertyValue("configList", managedList);
		}
	}
}
