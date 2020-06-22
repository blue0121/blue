package blue.internal.core.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.w3c.dom.Element;

/**
 * TaskScheduler解析器
 *
 * @author Jin Zheng
 * @date 2019-10-26
 */
public class TaskSchedulerParser extends SimpleBeanDefinitionParser
{
	public TaskSchedulerParser()
	{
		this.clazz = ThreadPoolTaskScheduler.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "poolSize", "pool-size");
	}

}
