package blue.internal.http.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * http自定义命名空间注册处理器
 *
 * @author Jin Zheng
 * @date 2018-10-25
 */
public class HttpNamespaceHandler extends NamespaceHandlerSupport
{
	public static final String NS = "http://blue.com/schema/http";

	public HttpNamespaceHandler()
	{
	}

	@Override
	public void init()
	{
		this.registerBeanDefinitionParser("controller-post-processor", new HttpPostProcessorParser());
		this.registerBeanDefinitionParser("http-config", new HttpConfigParser());
		this.registerBeanDefinitionParser("http-server", new HttpServerParser());
		this.registerBeanDefinitionParser("ssl-config", new SslConfigParser());
		this.registerBeanDefinitionParser("web-socket-config", new WebSocketConfigParser());
	}

}
