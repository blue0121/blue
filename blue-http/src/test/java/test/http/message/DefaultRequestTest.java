package test.http.message;

import blue.http.annotation.HttpMethod;
import blue.internal.http.message.DefaultRequest;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public class DefaultRequestTest
{
	private DefaultRequest request;

	public DefaultRequestTest()
	{
	}

	@BeforeEach
	public void before()
	{
		ChannelId id = Mockito.mock(ChannelId.class);
		request = new DefaultRequest(HttpMethod.POST, "localhost", id);
	}

	@Test
	public void testParseUri1()
	{
		String uri = "/api/hello?name=blue&pwd=%20pwd";
		request.parseUri(uri, "/api");
		Assertions.assertEquals("/api", request.getPath());
		Assertions.assertEquals("/hello", request.getUrl());
		Assertions.assertEquals(2, request.getQueryStringMap().size());
		Assertions.assertEquals("name=blue&pwd= pwd", request.getQueryString());
		Assertions.assertEquals("blue", request.getQueryString("name"));
		Assertions.assertEquals(" pwd", request.getQueryString("pwd"));
		Assertions.assertEquals("localhost", request.getIp());
		Assertions.assertEquals(HttpMethod.POST, request.getHttpMethod());
	}

	@Test
	public void testParseHeaders1()
	{
		HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		Mockito.when(headers.get(HttpHeaderNames.COOKIE)).thenReturn("BDSVRTM=532; PSTM=1542513710; ");
		request.parseHeaders(headers);
		Assertions.assertEquals(2, request.getCookieMap().size());
		Assertions.assertEquals("532", request.getCookie("BDSVRTM"));
		Assertions.assertEquals("1542513710", request.getCookie("PSTM"));
	}

}
