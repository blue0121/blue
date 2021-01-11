package blue.internal.http.ext.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * http-ext 自定义命名空间注册处理器
 *
 * @author Jin Zheng
 * @date 2018-10-25
 */
public class HttpExtNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/http-ext";

	public HttpExtNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("monitor-http-filter", new MonitorFilterParser());
		this.registerBeanDefinitionParser("monitor-controller", new MonitorControllerParser());
		this.registerBeanDefinitionParser("health-controller", new HealthControllerParser());
	}

}
