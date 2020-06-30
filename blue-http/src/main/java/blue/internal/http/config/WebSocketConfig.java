package blue.internal.http.config;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketConfig
{
	public static final String ROOT = "/ws";

	private String root = ROOT;
	private List<FilterConfig> filterConfigList;

	public WebSocketConfig()
	{
	}

	public String getRoot()
	{
		return root;
	}

	public void setRoot(String root)
	{
		this.root = root;
	}

	public List<FilterConfig> getFilterConfigList()
	{
		return filterConfigList;
	}

	public void setFilterConfigs(List<FilterConfig> filterConfigList)
	{
		this.filterConfigList = filterConfigList;
	}
}
