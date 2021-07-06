package blue.base.internal.starter.property;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-15
 */
public class HttpConfigProperties {
	private String id;
	private String baseUrl;
	private String username;
	private String password;
	private int timeout;
	private List<HttpHeaderProperties> headers;
	private String proxy;

	public HttpConfigProperties() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public List<HttpHeaderProperties> getHeaders() {
		return headers;
	}

	public void setHeaders(List<HttpHeaderProperties> headers) {
		this.headers = headers;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
}
