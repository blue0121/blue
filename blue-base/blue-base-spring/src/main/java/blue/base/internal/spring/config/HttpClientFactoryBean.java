package blue.base.internal.spring.config;

import blue.base.core.http.HttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-20
 */
public class HttpClientFactoryBean implements FactoryBean<HttpClient>, InitializingBean {
    private String baseUrl;
    private int timeout;
    private String proxy;
    private Map<String, String> defaultHeaders;
    private Executor executor;

    private HttpClient httpClient;

	public HttpClientFactoryBean() {
	}

    @Override
    public HttpClient getObject() throws Exception {
        return httpClient;
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.httpClient = HttpClient.create(baseUrl, timeout, proxy, defaultHeaders, executor);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
