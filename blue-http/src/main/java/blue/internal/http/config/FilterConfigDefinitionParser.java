package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jin Zheng
 * @since 2020-02-05
 */
public class FilterConfigDefinitionParser extends SimpleBeanDefinitionParser
{
	public FilterConfigDefinitionParser()
	{
	}

	protected void parseFilterConfig(Element root, BeanDefinition beanDefinition)
	{
		NodeList configList = root.getElementsByTagNameNS(HttpNamespaceHandler.NS, "filter-configs");
		if (configList.getLength() == 0)
			return;

		NodeList nodeList = configList.item(0).getChildNodes();
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
				definition.setBeanClass(FilterConfig.class);
				definition.getPropertyValues().addPropertyValue("filters", element.getAttribute("filters"));
				definition.getPropertyValues().addPropertyValue("excludes", element.getAttribute("excludes"));
				definition.getPropertyValues().addPropertyValue("order", element.getAttribute("order"));
				String ref = element.getAttribute("ref");
				definition.getPropertyValues().addPropertyValue("object", new RuntimeBeanReference(ref));

				BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, FilterConfig.class.getName() + "-" + ref);
				managedList.add(holder);
			}
		}
		if (!managedList.isEmpty())
		{
			beanDefinition.getPropertyValues().addPropertyValue("filterConfigs", managedList);
		}
	}

}
