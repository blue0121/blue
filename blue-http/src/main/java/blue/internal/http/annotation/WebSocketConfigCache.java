package blue.internal.http.annotation;

import blue.http.annotation.WebSocketUrlConfig;
import blue.internal.http.parser.WebSocketMethodResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class WebSocketConfigCache
{
	private Map<WebSocketUrlKey, WebSocketUrlConfig> cache = new HashMap<>();

	private static WebSocketConfigCache instance = new WebSocketConfigCache();

	private WebSocketConfigCache()
	{
	}

	public static WebSocketConfigCache getInstance()
	{
		return instance;
	}

	public void put(WebSocketUrlKey key,WebSocketUrlConfig config)
	{
		cache.put(key, config);
	}

	public boolean contains(WebSocketUrlKey key)
	{
		return cache.containsKey(key);
	}

	public WebSocketMethodResult getConfig(WebSocketUrlKey key)
	{
		WebSocketUrlConfig config = cache.get(key);
		if (config == null)
			return null;

		WebSocketMethodResult result = new WebSocketMethodResult(config);
		return result;
	}

	public Collection<WebSocketUrlConfig> all()
	{
		return cache.values();
	}

}
