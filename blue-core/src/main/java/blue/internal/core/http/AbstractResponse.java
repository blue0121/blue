package blue.internal.core.http;

import blue.core.http.Response;
import blue.core.util.StringUtil;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @date 2020-07-08
 */
public class AbstractResponse<T> implements Response<T>
{
	private int code;
	private Map<String, String> headers;
	private Map<String, List<String>> map;
	private T body;

	public AbstractResponse(HttpResponse<T> response)
	{
		this.code = response.statusCode();
		this.body = response.body();
		this.map = response.headers().map();
		this.initHeaders();
	}

	private void initHeaders()
	{
		if (map == null || map.isEmpty())
			return;

		headers = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : map.entrySet())
		{
			headers.put(entry.getKey(), StringUtil.join(entry.getValue(), CONCAT));
		}
	}

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public Map<String, String> getHeaders()
	{
		return headers;
	}

	@Override
	public Map<String, List<String>> getMap()
	{
		return map;
	}

	@Override
	public T getBody()
	{
		return body;
	}
}
