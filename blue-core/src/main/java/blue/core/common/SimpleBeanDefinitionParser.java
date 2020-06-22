package blue.core.common;

import blue.core.util.AssertUtil;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单解析器
 *
 * @author Jin Zheng
 * @since 2019-03-03
 */
public class SimpleBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser
{
	protected Class<?> clazz;

	public SimpleBeanDefinitionParser()
	{
	}

	@Override
	protected final Class<?> getBeanClass(Element element)
	{
		AssertUtil.notNull(clazz, "clazz");
		return clazz;
	}

	@Override
	protected final void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		AssertUtil.notNull(clazz, "clazz");
		try
		{
			this.doParseInternal(element, parserContext, builder);
		}
		catch (Exception e)
		{
			parserContext.getReaderContext().error(clazz.getName() + " 无法创建", element, null, e);
		}
	}

	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{

	}

	protected final List<Object> getBeanList(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, String namespace, String attribute)
	{
		List<Object> list = null;
		NodeList dictList = element.getElementsByTagNameNS(namespace, attribute);
		if (dictList != null && dictList.getLength() > 0)
		{
			Element node = (Element) dictList.item(0).getChildNodes().item(1);
			list = parserContext.getDelegate().parseListElement(node, builder.getRawBeanDefinition());
		}
		if (list == null)
		{
			list = new ArrayList<>();
		}
		return list;
	}

	protected final Map<Object, Object> getBeanMap(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, String namespace, String attribute)
	{
		Map<Object, Object> map = null;
		NodeList pkgList = element.getElementsByTagNameNS(namespace, attribute);
		if (pkgList.getLength() > 0)
		{
			Element node = (Element) pkgList.item(0).getChildNodes().item(1);
			 map = parserContext.getDelegate().parseMapElement(node, builder.getRawBeanDefinition());
		}
		if (map == null)
		{
			map = new HashMap<>();
		}
		return map;
	}

	protected final List<Object> parseList(List<Object> list, Element element, String attribute)
	{
		if (list == null)
		{
			list = new ArrayList<>();
		}
		String scan = element.getAttribute(attribute);
		if (scan != null && !scan.isEmpty())
		{
			String[] scans = scan.split("\\s|,|;");
			for (String s : scans)
			{
				s = s.trim();
				if (s != null && !s.isEmpty())
				{
					list.add(s);
				}
			}
		}
		return list;
	}

	protected final void parse(Element element, BeanDefinitionBuilder builder, String property, String attribute)
	{
		String attr = element.getAttribute(attribute);
		if (attr != null && !attr.isEmpty())
		{
			builder.addPropertyValue(property, attr);
		}
	}

	protected final void parse(Element element, RootBeanDefinition definition, String property, String attribute)
	{
		String attr = element.getAttribute(attribute);
		if (attr != null && !attr.isEmpty())
		{
			definition.getPropertyValues().addPropertyValue(property, attr);
		}
	}

	protected final void parseRef(Element element, BeanDefinitionBuilder builder, String property, String attribute)
	{
		String ref = element.getAttribute(attribute);
		if (ref != null && !ref.isEmpty())
		{
			builder.addPropertyValue(property, new RuntimeBeanReference(ref));
		}
	}

	protected final void parseRef(Element element, RootBeanDefinition definition, String property, String attribute)
	{
		String ref = element.getAttribute(attribute);
		if (ref != null && !ref.isEmpty())
		{
			definition.getPropertyValues().addPropertyValue(property, new RuntimeBeanReference(ref));
		}
	}

}
