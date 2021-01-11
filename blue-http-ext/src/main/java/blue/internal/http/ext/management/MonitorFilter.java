package blue.internal.http.ext.management;

import blue.core.util.NumberUtil;
import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;
import io.prometheus.client.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Arrays;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-09
 */
public class MonitorFilter implements HttpFilter, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(MonitorFilter.class);
	private static final double[] BUCKETS = {5, 10, 50, 100, 150, 200, 500, 1000, 2000, 5000};

	private ThreadLocal<Long> startLocal = new ThreadLocal<>();
	private Histogram histogram;
	private String buckets;

	public MonitorFilter()
	{
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
		histogram.labels(request.getUrl(), request.getHttpMethod().name(), "200")
				.observe(used / 1000.0d);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		double[] bucketArr;
		if (buckets == null || buckets.isEmpty())
		{
			bucketArr = BUCKETS;
		}
		else
		{
			bucketArr = NumberUtil.splitDouble(buckets);
		}
		this.histogram = Histogram.build("summary_http_request", "Http Request Latency Summary")
				.labelNames("path", "method", "code")
				.buckets(bucketArr)
				.register();
		logger.info("Initialize MonitorFilter, label: path, method, code, buckets: {}", Arrays.toString(bucketArr));
	}

	public void setBuckets(String buckets)
	{
		this.buckets = buckets;
	}
}
