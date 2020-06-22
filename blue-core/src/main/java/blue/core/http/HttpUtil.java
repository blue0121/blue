package blue.core.http;

import blue.core.dict.HttpMethod;
import blue.internal.core.http.MultiPartBodyPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jin Zheng
 * @since 2020-04-24
 */
public class HttpUtil implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private String baseUrl;
	private int timeout;
	private String proxy;
	private Map<String, String> defaultHeaders;
	private TaskExecutor taskExecutor;

	private HttpClient httpClient;

	public HttpUtil()
	{
	}

	public HttpResponse<String> requestSync(String uri)
	{
		return this.requestSync(uri, HttpMethod.GET, null, null);
	}

	public HttpResponse<String> requestSync(String uri, HttpMethod method)
	{
		return this.requestSync(uri, method, null, null);
	}

	public HttpResponse<String> requestSync(String uri, HttpMethod method, String body)
	{
		return this.requestSync(uri, method, body, null);
	}

	public HttpResponse<String> requestSync(String uri, HttpMethod method, String body, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);

		try
		{
			return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		}
		catch (Exception e)
		{
			this.handleException(e);
			return null;
		}
	}

	public CompletableFuture<HttpResponse<String>> requestAsync(String uri)
	{
		return this.requestAsync(uri, HttpMethod.GET, null, null);
	}

	public CompletableFuture<HttpResponse<String>> requestAsync(String uri, HttpMethod method)
	{
		return this.requestAsync(uri, method, null, null);
	}

	public CompletableFuture<HttpResponse<String>> requestAsync(String uri, HttpMethod method, String body)
	{
		return this.requestAsync(uri, method, body, null);
	}

	public CompletableFuture<HttpResponse<String>> requestAsync(String uri, HttpMethod method, String body, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	public HttpResponse<Path> downloadSync(String uri, Path file)
	{
		return this.downloadSync(uri, HttpMethod.GET, null, file, null);
	}

	public HttpResponse<Path> downloadSync(String uri, HttpMethod method, String body, Path file)
	{
		return this.downloadSync(uri, method, body, file);
	}

	public HttpResponse<Path> downloadSync(String uri, HttpMethod method, String body, Path file, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		try
		{
			return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofFile(file));
		}
		catch (Exception e)
		{
			this.handleException(e);
			return null;
		}
	}

	public CompletableFuture<HttpResponse<Path>> downloadAsync(String uri, Path file)
	{
		return this.downloadAsync(uri, HttpMethod.GET, null, file, null);
	}

	public CompletableFuture<HttpResponse<Path>> downloadAsync(String uri, HttpMethod method, String body, Path file)
	{
		return this.downloadAsync(uri, method, body, file, null);
	}

	public CompletableFuture<HttpResponse<Path>> downloadAsync(String uri, HttpMethod method, String body, Path file, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.publisher(body);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofFile(file));
	}

	public HttpResponse<String> uploadSync(String uri, HttpMethod method, Path file)
	{
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadSync(uri, method, null, fileParam, null);
	}

	public HttpResponse<String> uploadSync(String uri, HttpMethod method, Path file, Map<String, String> header)
	{
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadSync(uri, method, null, fileParam, header);
	}

	public HttpResponse<String> uploadSync(String uri, HttpMethod method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.buildBodyPublisher(textParam, fileParam);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		try
		{
			return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		}
		catch (Exception e)
		{
			this.handleException(e);
			return null;
		}
	}

	public CompletableFuture<HttpResponse<String>> uploadAsync(String uri, HttpMethod method, Path file)
	{
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadAsync(uri, method, null, fileParam, null);
	}

	public CompletableFuture<HttpResponse<String>> uploadAsync(String uri, HttpMethod method, Path file, Map<String, String> header)
	{
		Map<String, Path> fileParam = Map.of(file.getFileName().toString(), file);
		return this.uploadAsync(uri, method, null, fileParam, header);
	}

	public CompletableFuture<HttpResponse<String>> uploadAsync(String uri, HttpMethod method, Map<String, String> textParam, Map<String, Path> fileParam, Map<String, String> header)
	{
		HttpRequest.BodyPublisher publisher = this.buildBodyPublisher(textParam, fileParam);
		HttpRequest.Builder builder = this.builder(uri, method, header, publisher);
		return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	private HttpRequest.Builder builder(String uri, HttpMethod method, Map<String, String> header, HttpRequest.BodyPublisher publisher)
	{
		String url = (baseUrl != null && !baseUrl.isEmpty()) ? baseUrl + uri : uri;
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));
		Map<String, String> map = new HashMap<>();
		if (defaultHeaders != null && !defaultHeaders.isEmpty())
		{
			map.putAll(defaultHeaders);
		}
		if (header != null && !header.isEmpty())
		{
			map.putAll(header);
		}
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			builder.header(entry.getKey(), entry.getValue());
		}
		builder.method(method.getName(), publisher);
		builder.timeout(Duration.ofMillis(timeout));
		return builder;
	}

	private HttpRequest.BodyPublisher publisher(String body)
	{
		if (body != null && !body.isEmpty())
			return HttpRequest.BodyPublishers.ofString(body);

		return HttpRequest.BodyPublishers.noBody();
	}

	private void handleException(Exception cause)
	{
		logger.error("Error, ", cause);
		if (cause instanceof IOException)
		{
			throw new UncheckedIOException((IOException) cause);
		}
	}

	private HttpRequest.BodyPublisher buildBodyPublisher(Map<String, String> textParam, Map<String, Path> fileParam)
	{
		MultiPartBodyPublisher publisher = new MultiPartBodyPublisher();
		if (textParam != null && !textParam.isEmpty())
		{
			for (Map.Entry<String, String> entry : textParam.entrySet())
			{
				publisher.addPart(entry.getKey(), entry.getValue());
			}
		}
		if (fileParam != null && !fileParam.isEmpty())
		{
			for (Map.Entry<String, Path> entry : fileParam.entrySet())
			{
				publisher.addPart(entry.getKey(), entry.getValue());
			}
		}
		return publisher.build();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		HttpClient.Builder builder = HttpClient.newBuilder();
		if (timeout > 0)
		{
			builder.connectTimeout(Duration.ofMillis(timeout));
		}
		if (proxy != null && !proxy.isEmpty())
		{
			String[] proxys = proxy.split(":");
			if (proxys.length != 2) throw new IllegalArgumentException("Invalid proxy: " + proxy);

			builder.proxy(ProxySelector.of(new InetSocketAddress(proxys[0], Integer.parseInt(proxys[1]))));
		}
		if (taskExecutor != null)
		{
			builder.executor(taskExecutor);
		}
		this.httpClient = builder.build();
	}

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public String getProxy()
	{
		return proxy;
	}

	public void setProxy(String proxy)
	{
		this.proxy = proxy;
	}

	public Map<String, String> getDefaultHeaders()
	{
		return defaultHeaders;
	}

	public void setDefaultHeaders(Map<String, String> defaultHeaders)
	{
		this.defaultHeaders = defaultHeaders;
	}

	public TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}
}
