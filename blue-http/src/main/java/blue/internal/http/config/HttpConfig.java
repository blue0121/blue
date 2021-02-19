package blue.internal.http.config;

import blue.core.file.FilePath;
import blue.core.util.NumberUtil;
import blue.internal.http.annotation.FilterCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public class HttpConfig implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(HttpConfig.class);

	private String path; // 路径配置
	private Map<String, String> pathMap;  // 虚拟目录

	private long maxUploadSize;
	private List<FilterConfig> filterConfigList;

	private String ftlRoot;

	private FilterCache filterCache = FilterCache.getInstance();
	private FreeMarkerConfig freeMarkerConfig = FreeMarkerConfig.getInstance();

	public HttpConfig()
	{

	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (filterConfigList != null && !filterConfigList.isEmpty())
		{
			for (FilterConfig config : filterConfigList)
			{
				filterCache.addFilterConfig(config);
				logger.info("{} Filter name: {}, order: {}, include: {}, exclude: {}",
						config.getObject().getFilterType(), config.getClazz().getSimpleName(),
						config.getOrder(), config.getFilterList(), config.getExcludeList());
			}
		}

		logger.info("Max upload file size：{}", maxUploadSize > 0 ? NumberUtil.byteFormat(maxUploadSize) : "无限制");
		logger.info("App context path：{}", path);
		if (pathMap != null && !pathMap.isEmpty())
		{
			for (Map.Entry<String, String> entry : pathMap.entrySet())
			{
				logger.info("Virtual folder：{} ==> {}", entry.getKey(), entry.getValue());
			}
		}

		freeMarkerConfig.init(ftlRoot);
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		if (path == null || path.isEmpty())
			return;

		if (path.startsWith(FilePath.SLASH))
		{
			this.path = path;
		}
		else
		{
			this.path = FilePath.SLASH + path;
		}
	}

	public Map<String, String> getPathMap()
	{
		return pathMap;
	}

	public void setPathMap(Map<String, String> pathMap)
	{
		this.pathMap = pathMap;
	}

	public long getMaxUploadSize()
	{
		return maxUploadSize;
	}

	public void setMaxUploadSize(long maxUploadSize)
	{
		this.maxUploadSize = maxUploadSize;
	}

	public List<FilterConfig> getFilterConfigList()
	{
		return filterConfigList;
	}

	public void setFilterConfigs(List<FilterConfig> filterConfigList)
	{
		this.filterConfigList = filterConfigList;
	}

	public String getFtlRoot()
	{
		return ftlRoot;
	}

	public void setFtlRoot(String ftlRoot)
	{
		this.ftlRoot = ftlRoot;
	}
}
