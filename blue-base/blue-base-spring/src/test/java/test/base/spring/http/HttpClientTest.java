package test.base.spring.http;

import blue.base.core.dict.HttpMethod;
import blue.base.core.http.HttpClient;
import blue.base.core.http.StringResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jin Zheng
 * @since 2020-04-25
 */
@SpringJUnitConfig(locations = {"classpath:/spring/http.xml"})
public class HttpClientTest {
	private static final int PORT = 10000;

	private static WireMockServer server;

	private HttpClient httpClient;

	public HttpClientTest() {
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
	public void test() throws Exception {
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/test"))
				.willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody("test_content")));
		Assertions.assertEquals("test", httpClient.getUsername());
		Assertions.assertEquals("test", httpClient.getPassword());
		String auth = "Basic " + Base64.getEncoder().encodeToString("test:test".getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals(auth, httpClient.getHeaders().get("Authorization"));
		StringResponse response = httpClient.requestSync("/test", HttpMethod.GET.getName());
		Assertions.assertEquals(200, response.getCode());
		Assertions.assertEquals("test_content", response.getBody());

		CompletableFuture<StringResponse> future = httpClient.requestAsync("/test");
		future.thenAccept(r -> System.out.println(r.getCode() + ": " + r.getBody()));
		response = future.get();
		Assertions.assertEquals(200, response.getCode());
		Assertions.assertEquals("test_content", response.getBody());
	}

	@Autowired
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
