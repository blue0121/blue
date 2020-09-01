package blue.internal.http.filter;

import blue.core.util.WaitUtil;
import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2020-09-01
 */
public class SleepHttpFilter implements HttpFilter
{
	private static Logger logger = LoggerFactory.getLogger(SleepHttpFilter.class);

	private int millis = 1000;

	public SleepHttpFilter()
	{
	}

	@Override
	public boolean preHandle(Request request, Response response) throws Exception
	{
		if (millis > 0)
		{
			logger.info("Sleep {}ms", millis);
			WaitUtil.sleep(millis);
		}

		return true;
	}

	public void setMillis(int millis)
	{
		this.millis = millis;
	}
}
