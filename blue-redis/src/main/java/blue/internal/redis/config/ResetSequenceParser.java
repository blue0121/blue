package blue.internal.redis.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.redis.sequence.ResetSequence;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class ResetSequenceParser extends SimpleBeanDefinitionParser
{
	public ResetSequenceParser()
	{
		this.clazz = ResetSequence.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		List<Object> list = this.getBeanList(element, parserContext, builder, RedisNamespaceHandler.NS, "ref-sequences");
		builder.addPropertyValue("sequences", list);

		this.parseRef(element, builder, "taskScheduler", "ref-task-scheduler");
		this.parse(element, builder, "cron", "cron");
	}
}
