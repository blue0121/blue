package blue.internal.http.annotation;

import blue.http.annotation.HttpMethod;
import blue.http.annotation.HttpUrlConfig;
import blue.internal.http.parser.HttpMethodResult;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class HttpConfigCache
{
	public static final String BRACE_START = "{";
	public static final String BRACE_END = "}";

	private Map<HttpUrlKey, HttpUrlConfig> cache = new HashMap<>();
	private Map<HttpUrlKey, HttpUrlConfig> pathCache = new HashMap<>();

	private PathMatcher pathMatcher = new AntPathMatcher();

	private static HttpConfigCache instance = new HttpConfigCache();

	private HttpConfigCache()
	{
	}

	public static HttpConfigCache getInstance()
	{
		return instance;
	}

	public void put(HttpUrlKey key, HttpUrlConfig config)
	{
		cache.put(key, config);
		if (config.getUrl().contains(BRACE_START) && config.getUrl().contains(BRACE_END))
		{
			pathCache.put(key, config);
		}
	}

	public boolean contains(HttpUrlKey key)
	{
		return cache.containsKey(key);
	}

	public HttpMethodResult getConfig(String url, HttpMethod method)
	{
		return this.getConfig(new HttpUrlKey(url, method));
	}

	public HttpMethodResult getConfig(HttpUrlKey key)
	{
		HttpUrlConfig config = cache.get(key);
		if (config != null)
			return new HttpMethodResult(config, null);

		for (Map.Entry<HttpUrlKey, HttpUrlConfig> entry : pathCache.entrySet())
		{
			String url = entry.getKey().getUrl();
			if (pathMatcher.match(url, key.getUrl()))
			{
				Map<String, String> map = pathMatcher.extractUriTemplateVariables(url, key.getUrl());
				return new HttpMethodResult(entry.getValue(), map);
			}
		}
		return null;
	}

	public Collection<HttpUrlConfig> all()
	{
		return cache.values();
	}

}
