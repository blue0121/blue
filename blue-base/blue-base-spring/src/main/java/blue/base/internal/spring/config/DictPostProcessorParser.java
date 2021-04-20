package blue.base.internal.spring.config;

import blue.base.internal.spring.dict.DictPostProcessor;
import blue.base.spring.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-04-21
 */
public class DictPostProcessorParser extends SimpleBeanDefinitionParser {
	public DictPostProcessorParser() {
		this.clazz = DictPostProcessor.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		List<Object> list = this.getBeanList(element, parserContext, builder, CoreNamespaceHandler.NS, "scan-packages");
		list = this.parseList(list, element, "scan-packages");
		builder.addPropertyValue("scanPackages", list);
	}
}
