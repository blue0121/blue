package blue.base.core.http;

import blue.base.core.dict.HttpMethod;
import blue.base.internal.core.http.DefaultHttpClient;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public interface HttpClient {

	/**
	 * 创建Http客户端
	 *
	 * @param baseUrl
	 * @param timeout
	 * @param proxy
	 * @param defaultHeaders
	 * @param executor
	 * @return
	 */
	static HttpClient create(String baseUrl, int timeout, String proxy, Map<String, String> defaultHeaders, Executor executor) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.setBaseUrl(baseUrl);
		client.setTimeout(timeout);
		client.setProxy(proxy);
		client.setDefaultHeaders(defaultHeaders);
		client.setExecutor(executor);
		client.init();
		return client;
	}

	default StringResponse requestSync(String uri) {
		return this.requestSync(uri, HttpMethod.GET.getName(), null, null);
	}

	default StringResponse requestSync(String uri, String method) {
		return this.requestSync(uri, method, null, null);
	}

	default StringResponse requestSync(String uri, String method, String body) {
		return this.requestSync(uri, method, body, null);
	}

	StringResponse requestSync(String uri, String method, String body, Map<String, String> header);

	default CompletableFuture<StringResponse> requestAsync(String uri) {
		return this.requestAsync(uri, HttpMethod.GET.getName(), null, null);
	}

	default CompletableFuture<StringResponse> requestAsync(String uri, String method) {
		return this.requestAsync(uri, method, null, null);
	}

	default CompletableFuture<StringResponse> requestAsync(String uri, String method, String body) {
		return this.requestAsync(uri, method, body, null);
	}

	CompletableFuture<StringResponse> requestAsync(String uri, String method, String body, Map<String, String> header);

	default PathResponse downloadSync(String uri, Path file) {
		return this.downloadSync(uri, HttpMethod.GET.getName(), null, file, null);
	}

	default PathResponse downloadSync(String uri, String method, String body, Path file) {
		return this.downloadSync(uri, method, body, file, null);
	}

	PathResponse downloadSync(String uri, String method, String body, Path file, Map<String, String> header);

	default CompletableFuture<PathResponse> downloadAsync(String uri, Path file) {
		return this.downloadAsync(uri, HttpMethod.GET.getName(), null, file, null);
	}

	default CompletableFuture<PathResponse> downloadAsync(String uri, String method, String body, Path file) {
		return this.downloadAsync(uri, method, body, file, null);
	}

	CompletableFuture<PathResponse> downloadAsync(String uri, String method, String body, Path file, Map<String, String> header);

	default StringResponse uploadSync(String uri, String method, Path file) {
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadSync(uri, method, null, fileParam, null);
	}

	default StringResponse uploadSync(String uri, String method, Path file, Map<String, String> header) {
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadSync(uri, method, null, fileParam, header);
	}

	StringResponse uploadSync(String uri, String method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header);

	default CompletableFuture<StringResponse> uploadAsync(String uri, String method, Path file) {
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadAsync(uri, method, null, fileParam, null);
	}

	default CompletableFuture<StringResponse> uploadAsync(String uri, String method, Path file, Map<String, String> header) {
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadAsync(uri, method, null, fileParam, header);
	}

	CompletableFuture<StringResponse> uploadAsync(String uri, String method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header);

}
