package blue.internal.http.extension.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * http-extension自定义命名空间注册处理器
 *
 * @author Jin Zheng
 * @date 2018-10-25
 */
public class HttpExtensionNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/http-extension";

	public HttpExtensionNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("monitor-controller", new MonitorControllerParser());
		this.registerBeanDefinitionParser("monitor-http-filter", new MonitorFilterParser());
	}

}
