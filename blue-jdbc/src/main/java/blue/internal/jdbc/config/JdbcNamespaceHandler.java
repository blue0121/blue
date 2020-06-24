package blue.internal.jdbc.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * jdbc自定义命名空间注册处理器
 *
 * @author Jin Zheng
 * @date 2018-10-31
 */
public class JdbcNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/jdbc";

	public JdbcNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("data-source", new DataSourceParser());
		this.registerBeanDefinitionParser("route-data-source", new RouteDataSourceParser());
		this.registerBeanDefinitionParser("data-source-aspect", new DataSourceAspectParser());
		this.registerBeanDefinitionParser("transaction-manager", new TransactionManagerParser());
		this.registerBeanDefinitionParser("template", new TemplateParser());
	}
}
