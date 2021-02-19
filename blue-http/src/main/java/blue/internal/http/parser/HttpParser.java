package blue.internal.http.parser;

import blue.core.file.FilePath;
import blue.core.reflect.BeanMethod;
import blue.core.reflect.JavaBean;
import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.exception.HttpServerException;
import blue.internal.http.annotation.DefaultHttpUrlConfig;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.annotation.RequestParamConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-19
 */
public class HttpParser extends AbstractParser
{
	private static Logger logger = LoggerFactory.getLogger(HttpParser.class);

	private static final Set<HttpMethod> METHOD_SET = HttpMethod.all();

	private HttpConfigCache configCache = HttpConfigCache.getInstance();
	private Http annotationHttp;

	public HttpParser(JavaBean bean, Http annotationHttp)
	{
		super(bean);
		this.annotationHttp = annotationHttp;
	}

	@Override
	protected void parseMethod(BeanMethod method)
	{
		Set<HttpMethod> httpMethodSet = new HashSet<>();
		this.addHttpMethod(httpMethodSet, annotationHttp.method());

		Charset charset = annotationHttp.charset();
		ContentType contentType = annotationHttp.contentType();
		String name = annotationHttp.name();
		FilePath uriPath = this.rootPath.copy();
		Http annotationMethod = method.getAnnotation(Http.class);
		if (annotationMethod != null)
		{
			uriPath.concat(FilePath.SLASH, annotationMethod.url());
			charset = annotationMethod.charset();
			contentType = annotationMethod.contentType();
			name = annotationMethod.name();
			httpMethodSet.clear();
			this.addHttpMethod(httpMethodSet, annotationMethod.method());
		}
		if (httpMethodSet.isEmpty())
		{
			if (annotationHttp.method().length == 0)
			{
				httpMethodSet.addAll(METHOD_SET);
			}
			else
			{
				this.addHttpMethod(httpMethodSet, annotationHttp.method());
			}
		}
		List<RequestParamConfig> paramConfigList = PARAM_PARSER_FACTORY.parse(method);
		String url = uriPath.trim();
		for (HttpMethod httpMethod : httpMethodSet)
		{
			DefaultHttpUrlConfig config = new DefaultHttpUrlConfig();
			config.setName(name.isEmpty() ? url : name);
			config.setUrl(url);
			config.setHttpMethod(httpMethod);
			config.setCharset(charset);
			config.setContentType(contentType);
			config.setJavaBean(bean);
			config.setMethod(method);
			config.setParamList(paramConfigList);
			HttpUrlKey key = config.buildKey();

			if (configCache.contains(key))
				throw new HttpServerException("url 已经存在: " + config);

			configCache.put(key, config);
			logger.info("Found Http: {} [{}] [{}] [{}]，{}.{}()", config.getUrl(), httpMethod.name(),
					charset.name(), contentType, bean.getTargetClass().getSimpleName(), method.getName());
		}
	}

	@Override
	protected String getRootPath()
	{
		return annotationHttp.url();
	}

	private void addHttpMethod(Set<HttpMethod> httpMethodSet, HttpMethod[] httpMethods)
	{
		if (httpMethods == null || httpMethods.length == 0)
			return;

		for (HttpMethod httpMethod : httpMethods)
		{
			if (!METHOD_SET.contains(httpMethod))
				throw new HttpServerException("不支持 " + httpMethod.name());

			httpMethodSet.add(httpMethod);
		}
	}
}

