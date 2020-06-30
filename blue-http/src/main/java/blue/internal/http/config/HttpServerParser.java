package blue.internal.http.config;

import blue.core.common.SimpleBeanDefinitionParser;
import blue.internal.http.net.DefaultHttpServer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * HttpServer解析
 *
 * @author Jin Zheng
 * @date 2018-10-26
 */
public class HttpServerParser extends SimpleBeanDefinitionParser
{
	public HttpServerParser()
	{
		this.clazz = DefaultHttpServer.class;
	}

	@Override
	protected void doParseInternal(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
	{
		this.parse(element, builder, "port", "port");
		this.parse(element, builder, "ioThread", "io-thread");
		this.parseRef(element, builder, "taskExecutor", "ref-task-executor");
		this.parseRef(element, builder, "httpConfig", "ref-http-config");
		this.parseRef(element, builder, "webSocketConfig", "ref-web-socket-config");
		this.parseRef(element, builder, "sslConfig", "ref-ssl-config");
	}

}
