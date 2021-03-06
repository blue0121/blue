package test.base.core.http;

import blue.base.core.dict.HttpMethod;
import blue.base.core.http.HttpClient;
import blue.base.core.http.StringResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jin Zheng
 * @since 2020-04-25
 */
public class HttpInvokerTest {
	private static final int PORT = 10000;

	private static WireMockServer server;

	private HttpClient httpClient;

	public HttpInvokerTest() {
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

	@BeforeEach
	public void beforeEach() {
		Map<String, String> headers = Map.of("Content-Type", "application/json");
		httpClient = HttpClient.builder().setBaseUrl("http://localhost:10000")
				.setTimeout(10000)
				.setDefaultHeaders(headers)
				.build();
	}

	@Test
	public void test() throws Exception {
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/test"))
				.willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody("test_content")));
		StringResponse response = httpClient.requestSync("/test", HttpMethod.GET.getName());
		Assertions.assertEquals(200, response.getCode());
		Assertions.assertEquals("test_content", response.getBody());

		CompletableFuture<StringResponse> future = httpClient.requestAsync("/test");
		future.thenAccept(r -> System.out.println(r.getCode() + ": " + r.getBody()));
		response = future.get();
		Assertions.assertEquals(200, response.getCode());
		Assertions.assertEquals("test_content", response.getBody());
	}

}
