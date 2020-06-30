package blue.internal.http.config;

import blue.core.common.CoreConst;
import blue.http.exception.HttpServerException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.CharArrayWriter;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-19
 */
public class FreeMarkerConfig
{
	private static Logger logger = LoggerFactory.getLogger(FreeMarkerConfig.class);
	public static final String FTL_EXT = ".ftl";
	private static volatile FreeMarkerConfig instance;

	private Configuration config;

	private FreeMarkerConfig()
	{
	}

	public static FreeMarkerConfig getInstance()
	{
		if (instance == null)
		{
			synchronized (FreeMarkerConfig.class)
			{
				if (instance == null)
				{
					instance = new FreeMarkerConfig();
				}
			}
		}
		return instance;
	}

	void init(String root) throws TemplateException
	{
		if (root == null || root.isEmpty())
		{
			logger.info("FreeMarker root path: none");
			return;
		}

		config = new Configuration(Configuration.VERSION_2_3_29);
		config.setDefaultEncoding(CoreConst.UTF_8);
		config.setClassForTemplateLoading(HttpConfig.class, root);

		config.setSetting("defaultEncoding", "UTF-8");
		config.setSetting("number_format", "#.##");
		config.setSetting("datetime_format", "yyyy-MM-dd HH:mm");
		config.setSetting("date_format", "yyyy-MM-dd");
		config.setSetting("time_format", "HH:mm:ss");

		logger.info("FreeMarker root path: {}", root);
	}

	public String render(String ftl, Map<String, Object> param)
	{
		if (config == null)
			throw new HttpServerException("FreeMarker 配置未初始化");

		String html = null;
		try (CharArrayWriter writer = new CharArrayWriter())
		{
			Template template = config.getTemplate(ftl + FTL_EXT);
			template.process(param, writer);
			html = writer.toString();
		}
		catch (Exception e)
		{
			logger.error("渲染页面出错：" + ftl, e);
		}
		return html;
	}

}
