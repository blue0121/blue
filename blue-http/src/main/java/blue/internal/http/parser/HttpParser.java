package blue.internal.http.parser;

import blue.core.util.UrlUtil;
import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.exception.HttpServerException;
import blue.internal.http.annotation.DefaultHttpUrlConfig;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.annotation.HttpUrlKey;
import blue.internal.http.annotation.RequestParamConfig;
import blue.internal.http.parser.parameter.ParamParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpParser
{
	public static final String SPLIT = "/";
	private static Logger logger = LoggerFactory.getLogger(HttpParser.class);

	private Set<Class<?>> clazzSet = new HashSet<>();

	private HttpConfigCache configCache = HttpConfigCache.getInstance();
	private Set<HttpMethod> methodSet = HttpMethod.all();

	private static HttpParser instance = new HttpParser();

	private HttpParser()
	{
	}

	public static HttpParser getInstance()
	{
		return instance;
	}

	public void parse(Object target, Class<?> clazz)
	{
		if (clazzSet.contains(clazz))
			return;

		Http annoHttp = clazz.getAnnotation(Http.class);
		if (annoHttp == null)
			throw new HttpServerException(clazz.getName() + " 缺少 @Http 注解");

		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getModifiers() != Modifier.PUBLIC)
				continue;

			this.parseMethod(target, method, annoHttp);
		}

		clazzSet.add(clazz);
	}

	private void parseMethod(Object target, Method method, Http annoHttp)
	{
		List<String> urlList = new ArrayList<>();
		urlList.add(SPLIT);
		urlList.add(annoHttp.url());
		Set<HttpMethod> httpMethodSet = new HashSet<>();
		this.addHttpMethod(httpMethodSet, annoHttp.method());

		Charset charset = annoHttp.charset();
		ContentType contentType = annoHttp.contentType();
		String name = annoHttp.name();

		Http annoMethod = method.getAnnotation(Http.class);
		if (annoMethod != null) // 方法配置覆盖类配置
		{
			urlList.add(SPLIT);
			urlList.add(annoMethod.url());
			charset = annoMethod.charset();
			contentType = annoMethod.contentType();
			name = annoMethod.name();
			httpMethodSet.clear();
			this.addHttpMethod(httpMethodSet, annoMethod.method());
		}
		String url = UrlUtil.concat(urlList.toArray(new String[0]));

		if (httpMethodSet.isEmpty()) // 没有关联httpMethod
		{
			httpMethodSet.addAll(methodSet);
		}
		List<RequestParamConfig> paramConfigList = ParamParserFactory.getInstance().parse(method);
		for (HttpMethod httpMethod : httpMethodSet)
		{
			DefaultHttpUrlConfig config = new DefaultHttpUrlConfig();
			config.setName(name.isEmpty() ? url : name);
			config.setUrl(url);
			config.setHttpMethod(httpMethod);
			config.setCharset(charset);
			config.setContentType(contentType);
			config.setTarget(target);
			config.setMethod(method);
			config.setParamList(paramConfigList);
			HttpUrlKey key = config.buildKey();

			if (configCache.contains(key))
				throw new HttpServerException("url 已经存在: " + config);

			configCache.put(key, config);
			logger.info("Found Http: {} [{}] [{}] [{}]，{}.{}()", config.getUrl(), httpMethod.name(),
					charset.name(), contentType, method.getDeclaringClass().getSimpleName(), method.getName());
		}
	}

	private void addHttpMethod(Set<HttpMethod> httpMethodSet, HttpMethod[] httpMethods)
	{
		if (httpMethods == null || httpMethods.length == 0)
			return;

		for (HttpMethod httpMethod : httpMethods)
		{
			if (!methodSet.contains(httpMethod))
				throw new HttpServerException("不支持 " + httpMethod.name());

			httpMethodSet.add(httpMethod);
		}
	}

}
