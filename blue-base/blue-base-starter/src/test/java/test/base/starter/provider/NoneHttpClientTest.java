package test.base.starter.provider;

import blue.base.core.http.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.base.starter.HttpClientApplication;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-27
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test2")
@SpringBootTest(classes = HttpClientApplication.class)
public class NoneHttpClientTest {
	private HttpClient httpClient;

	public NoneHttpClientTest() {
	}

	@Test
	public void test() {
		Assertions.assertNull(httpClient);
	}

	@Autowired(required = false)
	@Qualifier("test1")
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
