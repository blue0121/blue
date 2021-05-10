package blue.base.spring.common;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jin Zheng
 * @since 2019-04-06
 */
public class ListenerContainerParser extends SimpleBeanDefinitionParser {
	public ListenerContainerParser() {
	}

	protected final void parseListenerConfig(NodeList nodeList, BeanDefinition beanDefinition, Class<?> clazz) {
		if (nodeList == null || nodeList.getLength() == 0) {
			return;
		}

		ManagedList<BeanDefinitionHolder> managedList = new ManagedList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				RootBeanDefinition definition = new RootBeanDefinition();
				definition.setBeanClass(clazz);
				definition.getPropertyValues().addPropertyValue("topic", element.getAttribute("topic"));
				definition.getPropertyValues().addPropertyValue("multiThread", element.getAttribute("multi-thread"));
				this.parseRef(element, definition, "listener", "ref-listener");
				this.parseRef(element, definition, "executor", "ref-executor");
				this.parseRef(element, definition, "exceptionHandler", "ref-exception-handler");
				this.doParseListenerConfig(element, definition, clazz);

				BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, clazz.getName());
				managedList.add(holder);
			}
		}
		if (!managedList.isEmpty()) {
			beanDefinition.getPropertyValues().addPropertyValue("configList", managedList);
		}
	}

	protected void doParseListenerConfig(Element element, RootBeanDefinition definition, Class<?> clazz) {

	}

}
