package blue.internal.http.annotation;

import blue.http.filter.Filter;
import blue.http.filter.FilterType;
import blue.internal.http.config.FilterConfig;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class FilterCache
{
	private SortedSet<FilterConfig> httpFilterSet = new TreeSet<>();
	private SortedSet<FilterConfig> webSocketFilterSet = new TreeSet<>();
	private PathMatcher pathMatcher = new AntPathMatcher();

	private static FilterCache instance = new FilterCache();

	private FilterCache()
	{
	}

	public static FilterCache getInstance()
	{
		return instance;
	}

	public void addFilterConfig(FilterConfig config)
	{
		FilterType type = config.getObject().getFilterType();
		if (type == FilterType.HTTP)
		{
			httpFilterSet.add(config);
		}
		else if (type == FilterType.WEB_SOCKET)
		{
			webSocketFilterSet.add(config);
		}
	}

	public List<Filter<Object, Object>> matchFilter(String url, FilterType type)
	{
		List<Filter<Object, Object>> list = new ArrayList<>();
		SortedSet<FilterConfig> configList = null;
		if (type == FilterType.HTTP)
		{
			configList = httpFilterSet;
		}
		else if (type == FilterType.WEB_SOCKET)
		{
			configList = webSocketFilterSet;
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
