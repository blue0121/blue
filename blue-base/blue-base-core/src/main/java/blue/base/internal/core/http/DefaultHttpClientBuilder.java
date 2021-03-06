package blue.base.internal.core.http;

import blue.base.core.http.HttpClient;
import blue.base.core.http.HttpClientBuilder;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 2021-07-02
 */
public class DefaultHttpClientBuilder implements HttpClientBuilder {
    private String id;
    private String baseUrl;
    private int timeout;
    private String username;
    private String password;
    private String proxy;
    private Map<String, String> headers;
    private Executor executor;

	public DefaultHttpClientBuilder() {
	}

    @Override
    public HttpClient build() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.setId(id);
        client.setBaseUrl(baseUrl);
        client.setTimeout(timeout);
        client.setUsername(username);
        client.setPassword(password);
        client.setProxy(proxy);
        client.setDefaultHeaders(headers);
        client.setExecutor(executor);
        client.init();
        return client;
    }

    @Override
    public HttpClientBuilder setId(String id) {
	    this.id = id;
        return this;
    }

    @Override
    public HttpClientBuilder setBaseUrl(String baseUrl) {
	    this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public HttpClientBuilder setTimeout(int timeout) {
	    this.timeout = timeout;
        return this;
    }

    @Override
    public HttpClientBuilder setUsername(String username) {
	    this.username = username;
        return this;
    }

    @Override
    public HttpClientBuilder setPassword(String password) {
	    this.password = password;
        return this;
    }

    @Override
    public HttpClientBuilder setProxy(String proxy) {
	    this.proxy = proxy;
        return this;
    }

    @Override
    public HttpClientBuilder setDefaultHeaders(Map<String, String> headers) {
	    this.headers = headers;
        return this;
    }

    @Override
    public HttpClientBuilder setExecutor(Executor executor) {
	    this.executor = executor;
        return this;
    }
}
