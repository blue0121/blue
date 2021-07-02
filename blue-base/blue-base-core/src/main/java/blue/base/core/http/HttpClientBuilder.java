package blue.base.core.http;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 2021-07-02
 */
public interface HttpClientBuilder {

	HttpClient build();

	HttpClientBuilder setBaseUrl(String baseUrl);

	HttpClientBuilder setTimeout(int timeout);

	HttpClientBuilder setProxy(String proxy);

	HttpClientBuilder setDefaultHeaders(Map<String, String> headers);

	HttpClientBuilder setExecutor(Executor executor);

}
