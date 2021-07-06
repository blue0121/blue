package test.base.starter.provider;

import blue.base.core.http.HttpClient;
import blue.base.core.http.StringResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.base.starter.HttpClientApplication;
import test.base.starter.service.HttpClientService;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-27
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test1")
@SpringBootTest(classes = HttpClientApplication.class)
public class HttpClientServiceTest {
	private static final int PORT = 9090;
	private static WireMockServer server;

	private HttpClientService service;
	private HttpClient httpClient;
	private HttpClient noneHttpClient;

	public HttpClientServiceTest() {
	}

	@BeforeAll
	public static void beforeAll() {
		server = new WireMockServer(PORT);
		server.start();
		WireMock.configureFor(PORT);
	}

	@AfterAll
	public static void afterAll() {
		server.stop();
	}

	@Test
	public void test1() {
		HttpClient httpClient = service.getHttpClient();
		Assertions.assertEquals("test1", httpClient.getId());
		Assertions.assertEquals("http://localhost:8080", httpClient.getBaseUrl());
		Assertions.assertEquals("test1", httpClient.getUsername());
		Assertions.assertEquals("test1", httpClient.getPassword());
		Assertions.assertEquals(60000, httpClient.getTimeout());
		Assertions.assertEquals("localhost:8000", httpClient.getProxy());
		System.out.println(httpClient.getHeaders());
		Assertions.assertEquals("application/json", httpClient.getHeaders().get("Accept"));
		Assertions.assertEquals("application/json", httpClient.getHeaders().get("Content-Type"));
	}

	@Test
	public void test2() {
		Assertions.assertEquals("test2", httpClient.getUsername());
		Assertions.assertEquals("test2", httpClient.getPassword());
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/test")).willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json").withBody("test_content")));
		StringResponse response = httpClient.requestSync("/test", "GET");
		Assertions.assertEquals(200, response.getCode());
		Assertions.assertEquals("test_content", response.getBody());
	}

	@Test
	public void test3() {
		Assertions.assertNull(noneHttpClient);
	}

	@Autowired
	public void setService(HttpClientService service) {
		this.service = service;
	}

	@Autowired
	@Qualifier("test2")
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Autowired(required = false)
	@Qualifier("test3")
	public void setNoneHttpClient(HttpClient noneHttpClient) {
		this.noneHttpClient = noneHttpClient;
	}
}
