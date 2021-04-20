package blue.base.internal.core.http;


import blue.base.core.http.HttpClient;
import blue.base.core.http.PathResponse;
import blue.base.core.http.StringResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public class DefaultHttpClient implements HttpClient {
	private static Logger logger = LoggerFactory.getLogger(DefaultHttpClient.class);

	private String baseUrl;
	private int timeout;
	private String proxy;
	private Map<String, String> defaultHeaders;
	private Executor executor;

	private java.net.http.HttpClient httpClient;

	public DefaultHttpClient() {
	}

	@Override
	public StringResponse requestSync(String uri, String method, String body, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);

		try {
			HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
			return new DefaultStringResponse(response);
		}
		catch (Exception e) {
			this.handleException(e);
			return null;
		}
	}

	@Override
	public CompletableFuture<StringResponse> requestAsync(String uri, String method, String body, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
		return future.thenApply(s -> new DefaultStringResponse(s));
	}

	@Override
	public PathResponse downloadSync(String uri, String method, String body, Path file, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		try {
			HttpResponse<Path> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofFile(file));
			return new DefaultPathResponse(response);
		}
		catch (Exception e) {
			this.handleException(e);
			return null;
		}
	}

	@Override
	public CompletableFuture<PathResponse> downloadAsync(String uri, String method, String body, Path file, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		CompletableFuture<HttpResponse<Path>> future = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofFile(file));
		return future.thenApply(s -> new DefaultPathResponse(s));
	}

	@Override
	public StringResponse uploadSync(String uri, String method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.buildBodyPublisher(textParam, fileParam);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		try {
			HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
			return new DefaultStringResponse(response);
		}
		catch (Exception e) {
			this.handleException(e);
			return null;
		}
	}

	@Override
	public CompletableFuture<StringResponse> uploadAsync(String uri, String method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header) {
		HttpRequest.BodyPublisher publisher = this.buildBodyPublisher(textParam, fileParam);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
		return future.thenApply(s -> new DefaultStringResponse(s));
	}

	private HttpRequest.Builder builder(String uri, String method, Map<String, String> header, HttpRequest.BodyPublisher publisher) {
		String url = (baseUrl != null && !baseUrl.isEmpty()) ? baseUrl + uri : uri;
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));
		Map<String, String> map = new HashMap<>();
		if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
			map.putAll(defaultHeaders);
		}
		if (header != null && !header.isEmpty()) {
			map.putAll(header);
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.header(entry.getKey(), entry.getValue());
		}
		builder.method(method, publisher);
		builder.timeout(Duration.ofMillis(timeout));
		return builder;
	}

	private HttpRequest.BodyPublisher publisher(String body) {
		if (body != null && !body.isEmpty()) {
			return HttpRequest.BodyPublishers.ofString(body);
		}

		return HttpRequest.BodyPublishers.noBody();
	}

	private void handleException(Exception cause) {
		logger.error("Error, ", cause);
		if (cause instanceof IOException) {
			throw new UncheckedIOException((IOException) cause);
		}
	}

	private HttpRequest.BodyPublisher buildBodyPublisher(Map<String, String> textParam, Map<String, Path> fileParam) {
		MultiPartBodyPublisher publisher = new MultiPartBodyPublisher();
		if (textParam != null && !textParam.isEmpty()) {
			for (Map.Entry<String, String> entry : textParam.entrySet()) {
				publisher.addPart(entry.getKey(), entry.getValue());
			}
		}
		if (fileParam != null && !fileParam.isEmpty()) {
			for (Map.Entry<String, Path> entry : fileParam.entrySet()) {
				publisher.addPart(entry.getKey(), entry.getValue());
			}
		}
		return publisher.build();
	}

	public void init() {
		java.net.http.HttpClient.Builder builder = java.net.http.HttpClient.newBuilder();
		if (timeout > 0) {
			builder.connectTimeout(Duration.ofMillis(timeout));
		}
		if (proxy != null && !proxy.isEmpty()) {
			String[] proxys = proxy.split(":");
			if (proxys.length != 2) {
				throw new IllegalArgumentException("Invalid proxy: " + proxy);
			}

			builder.proxy(ProxySelector.of(new InetSocketAddress(proxys[0], Integer.parseInt(proxys[1]))));
		}
		if (executor != null) {
			builder.executor(executor);
		}
		this.httpClient = builder.build();
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
