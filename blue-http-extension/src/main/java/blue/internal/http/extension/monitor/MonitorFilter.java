package blue.internal.http.extension.monitor;

import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;
import io.prometheus.client.Summary;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-09
 */
public class MonitorFilter implements HttpFilter
{
	private ThreadLocal<Long> startLocal;
	private Summary summary;

	public MonitorFilter()
	{
		startLocal = new ThreadLocal<>();
		summary = Summary.build("summary_http_request", "Http Request Latency Summary")
				.labelNames("path", "method", "code")
				.register();
	}

	@Override
	public boolean preHandle(Request request, Response response) throws Exception
	{
		startLocal.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void afterCompletion(Request request, Response response, Exception ex) throws Exception
	{
		long start = startLocal.get();
		startLocal.remove();
		long used = System.currentTimeMillis() - start;
		summary.labels(request.getUrl(), request.getHttpMethod().name(), "200")
				.observe(used / 1000.0d);
	}
}
