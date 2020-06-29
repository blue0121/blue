package blue.internal.http.filter;

import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-10
 */
public class SettingFilter implements HttpFilter
{
	private Map<String, String> param;

	public SettingFilter()
	{
	}

	@Override
	public boolean preHandle(Request request, Response response) throws Exception
	{
		if (param != null && !param.isEmpty())
		{
			response.getParam().putAll(param);
		}

		return true;
	}

	public void setParam(Map<String, String> param)
	{
		this.param = param;
	}

}
