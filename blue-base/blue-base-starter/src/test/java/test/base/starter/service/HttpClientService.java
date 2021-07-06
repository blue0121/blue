package test.base.starter.service;

import blue.base.core.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-27
 */
@Component
public class HttpClientService {
	private HttpClient httpClient;

	public HttpClientService() {
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	@Autowired(required = false)
	@Qualifier("test1")
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
}
