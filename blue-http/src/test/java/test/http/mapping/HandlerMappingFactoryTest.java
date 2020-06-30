package test.http.mapping;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.http.message.Request;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.mapping.HandlerMappingFactory;
import blue.internal.http.parser.HttpMethodResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.http.BaseTest;
import test.http.controller.EchoController;
import test.http.controller.HelloController;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HandlerMappingFactoryTest extends BaseTest
{
	private HandlerMappingFactory factory = HandlerMappingFactory.getInstance();

	private Request request;

	public HandlerMappingFactoryTest()
	{
	}

	@BeforeEach
	public void before()
	{
		this.request = Mockito.mock(Request.class);
	}

	@Test
	public void testGetHandlerChain1()
	{
		Mockito.when(request.getUrl()).thenReturn("/echo");
		Mockito.when(request.getHttpMethod()).thenReturn(HttpMethod.POST);

		HandlerChain chain = factory.getHandlerChain(request);
		Assertions.assertNotNull(chain);
		HttpMethodResult result = (HttpMethodResult) chain.getHandler();
		Assertions.assertEquals(Charset.UTF_8, result.getCharset());
		Assertions.assertEquals(ContentType.AUTO, result.getContentType());
		Assertions.assertEquals(EchoController.class, result.getMethod().getDeclaringClass());
		Assertions.assertEquals("echo", result.getMethod().getName());
	}

	@Test
	public void testGetHandlerChain2()
	{
		Mockito.when(request.getUrl()).thenReturn("/hello/hello2");
		Mockito.when(request.getHttpMethod()).thenReturn(HttpMethod.POST);

		HandlerChain chain = factory.getHandlerChain(request);
		Assertions.assertNotNull(chain);
		HttpMethodResult result = (HttpMethodResult) chain.getHandler();
		Assertions.assertEquals(Charset.UTF_8, result.getCharset());
		Assertions.assertEquals(ContentType.AUTO, result.getContentType());
		Assertions.assertEquals(HelloController.class, result.getMethod().getDeclaringClass());
		Assertions.assertEquals("hello2", result.getMethod().getName());
	}

}
