package blue.internal.core.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.w3c.dom.Element;

/**
 * TaskExecutor解析器
 *
 * @author Jin Zheng
 * @date 2018-10-26
 */
public class TaskExecutorParser extends SimpleBeanDefinitionParser
{
	public TaskExecutorParser()
	{
		this.clazz = ThreadPoolTaskExecutor.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "corePoolSize", "core-pool-size");
		this.parse(element, builder, "maxPoolSize", "max-pool-size");
		this.parse(element, builder, "queueCapacity", "queue-capacity");
		this.parse(element, builder, "keepAliveSeconds", "keep-alive-seconds");
	}

}
