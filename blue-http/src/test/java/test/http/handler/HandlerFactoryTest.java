package test.http.handler;

import blue.core.common.ErrorCode;
import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.http.message.Request;
import blue.http.message.Response;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.handler.HandlerFactory;
import blue.internal.http.mapping.HandlerMappingFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.http.BaseTest;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HandlerFactoryTest extends BaseTest
{
	private HandlerMappingFactory mappingFactory = HandlerMappingFactory.getInstance();
	private HandlerFactory handlerFactory = HandlerFactory.getInstance();

	private Request request;

	public HandlerFactoryTest()
	{
	}

	@BeforeEach
	public void before()
	{
		this.request = Mockito.mock(Request.class);
	}

	@Test
	public void testEcho()
	{
		Mockito.when(request.getUrl()).thenReturn("/echo");
		Mockito.when(request.getHttpMethod()).thenReturn(HttpMethod.POST);
		Mockito.when(request.getContent()).thenReturn("blue");

		HandlerChain chain = mappingFactory.getHandlerChain(request);
		Assertions.assertNotNull(chain);
		Response response = handlerFactory.handle(request, chain);
		Assertions.assertNotNull(response.getResult());
		Assertions.assertEquals("blue", response.getResult());
		Assertions.assertEquals(Charset.UTF_8, response.getCharset());
		Assertions.assertEquals(ContentType.AUTO, response.getContentType());
	}

	@Test
	public void testEcho2()
	{
		Mockito.when(request.getUrl()).thenReturn("/echo");
		Mockito.when(request.getHttpMethod()).thenReturn(HttpMethod.POST);

		HandlerChain chain = mappingFactory.getHandlerChain(request);
		Assertions.assertNotNull(chain);
		Response response = handlerFactory.handle(request, chain);
		System.out.println(response.getResult());
		Assertions.assertNotNull(response.getResult());
		Assertions.assertNotNull(response.getCause());
		Assertions.assertEquals(ValidationException.class, response.getCause().getClass());
		JSONObject json = JSON.parseObject(response.getResult().toString());
		Assertions.assertEquals(ErrorCode.INVALID_PARAM.getCode(), json.getIntValue(ErrorCode.CODE));
	}

	@Test
	public void testHello()
	{
		Mockito.when(request.getUrl()).thenReturn("/hello/hello2");
		Mockito.when(request.getHttpMethod()).thenReturn(HttpMethod.POST);
		Mockito.when(request.getContent()).thenReturn("blue");

		HandlerChain chain = mappingFactory.getHandlerChain(request);
		Assertions.assertNotNull(chain);
		Response response = handlerFactory.handle(request, chain);
		Assertions.assertNotNull(response.getResult());
		Assertions.assertEquals("hello2, blue", response.getResult());
		Assertions.assertEquals(Charset.UTF_8, response.getCharset());
		Assertions.assertEquals(ContentType.AUTO, response.getContentType());
	}

}
