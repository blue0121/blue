package blue.internal.http.parser;

import blue.http.annotation.HttpMethod;
import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.internal.http.config.FilterConfig;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class ParserCache
{
	public static final String BRACE_START = "{";
	public static final String BRACE_END = "}";

	private Map<HttpUrlConfig, HttpUrlMethod> configMap = new HashMap<>();
	private Map<HttpUrlConfig, HttpUrlMethod> pathConfigMap = new HashMap<>();
	private SortedSet<FilterConfig> filterConfigList = new TreeSet<>();
	private PathMatcher pathMatcher = new AntPathMatcher();

	private Map<WebSocketUrlConfig, WebSocketMethod> webSocketMap = new HashMap<>();
	private SortedSet<FilterConfig> webSocketFilterConfigList = new TreeSet<>();

	private static volatile ParserCache instance;

	private ParserCache()
	{
	}

	public static ParserCache getInstance()
	{
		if (instance == null)
		{
			synchronized (ParserCache.class)
			{
				if (instance == null)
				{
					instance = new ParserCache();
				}
			}
		}
		return instance;
	}

	public void putConfig(HttpUrlConfig config, HttpUrlMethod method)
	{
		configMap.put(config, method);
		if (config.getUrl().contains(BRACE_START) && config.getUrl().contains(BRACE_END))
		{
			pathConfigMap.put(config, method);
		}
	}

	public boolean containsConfig(HttpUrlConfig config)
	{
		return configMap.containsKey(config);
	}

	public HttpMethodResult getConfig(String url, HttpMethod method)
	{
		return this.getConfig(new HttpUrlConfig(url, method));
	}

	public HttpMethodResult getConfig(HttpUrlConfig config)
	{
		HttpUrlMethod method = configMap.get(config);
		if (method != null)
			return new HttpMethodResult(method, null);

		for (Map.Entry<HttpUrlConfig, HttpUrlMethod> entry : pathConfigMap.entrySet())
		{
			String url = entry.getKey().getUrl();
			if (pathMatcher.match(url, config.getUrl()))
			{
				Map<String, String> map = pathMatcher.extractUriTemplateVariables(url, config.getUrl());
				return new HttpMethodResult(entry.getValue(), map);
			}
		}
		return null;
	}

	public void putConfig(WebSocketUrlConfig config, WebSocketMethod method)
	{
		webSocketMap.put(config, method);
	}

	public boolean containsConfig(WebSocketUrlConfig config)
	{
		return webSocketMap.containsKey(config);
	}

	public WebSocketMethodResult getConfig(WebSocketUrlConfig config)
	{
		WebSocketMethod method = webSocketMap.get(config);
		if (method == null)
			return null;

		WebSocketMethodResult result = new WebSocketMethodResult(method.getMethod());
		return result;
	}

	public int configSize()
	{
		return configMap.size();
	}

	public void clear()
	{
		configMap.clear();
		pathConfigMap.clear();
		filterConfigList.clear();
		webSocketMap.clear();
	}

	public void addFilterConfig(FilterConfig config)
	{
		FilterType type = config.getObject().getFilterType();
		if (type == FilterType.HTTP)
		{
			filterConfigList.add(config);
		}
		else if (type == FilterType.WEB_SOCKET)
		{
			webSocketFilterConfigList.add(config);
		}
	}

	public List<Filter<Object, Object>> matchFilter(String url, FilterType type)
	{
		List<Filter<Object, Object>> list = new ArrayList<>();
		SortedSet<FilterConfig> configList = null;
		if (type == FilterType.HTTP)
		{
			configList = filterConfigList;
		}
		else if (type == FilterType.WEB_SOCKET)
		{
			configList = webSocketFilterConfigList;
		}
		if (configList == null || configList.isEmpty())
			return list;

		for (FilterConfig config : configList)
		{
			boolean isExclude = false;
			for (String exclude : config.getExcludeList())
			{
				if (pathMatcher.match(exclude, url))
				{
					isExclude = true;
					break;
				}
			}
			if (isExclude)
				continue;

			for (String include : config.getFilterList())
			{
				if (pathMatcher.match(include, url))
				{
					list.add(config.getObject());
					break;
				}
			}
		}
		return list;
	}

}
