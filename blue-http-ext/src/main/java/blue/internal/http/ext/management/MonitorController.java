package blue.internal.http.ext.management;

import blue.core.common.CoreConst;
import blue.http.annotation.ContentType;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.message.Request;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import io.prometheus.client.hotspot.DefaultExports;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-09
 */
@Controller
@Http(url = "/management/monitor", method = HttpMethod.GET, contentType = ContentType.HTML)
public class MonitorController
{
	private CollectorRegistry registry;

	public MonitorController()
	{
		DefaultExports.initialize();
		this.registry = CollectorRegistry.defaultRegistry;
	}

	public String index(Request request)
	{
		String text = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     PrintWriter pw = new PrintWriter(baos))
		{
			TextFormat.write004(pw, this.registry.metricFamilySamples());
			pw.flush();
			text = baos.toString(CoreConst.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return text;
	}

}
