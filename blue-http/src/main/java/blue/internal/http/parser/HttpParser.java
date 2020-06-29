package blue.internal.http.parser;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.exception.HttpServerException;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpParser
{
	public static final String SPLIT = "/";
	private static Logger logger = LoggerFactory.getLogger(HttpParser.class);

	private Set<Class<?>> paramSet = new HashSet<>();
	private String paramString;
	private Set<Class<?>> clazzSet = new HashSet<>();

	private ParserCache parserCache = ParserCache.getInstance();
	private Set<HttpMethod> methodSet = new HashSet<>();

	private static volatile HttpParser instance;

	private HttpParser()
	{
		paramSet.add(String.class);
		paramSet.add(Request.class);
		paramSet.add(UploadFile.class);

		paramString = paramSet.toString();

		methodSet.add(HttpMethod.GET);
		methodSet.add(HttpMethod.POST);
		methodSet.add(HttpMethod.PUT);
		methodSet.add(HttpMethod.DELETE);
	}

	public static HttpParser getInstance()
	{
		if (instance == null)
		{
			synchronized (HttpParser.class)
			{
				if (instance == null)
				{
					instance = new HttpParser();
				}
			}
		}
		return instance;
	}

	public void parse(Class<?> clazz)
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

			if (method.getParameterCount() > 1)
				throw new HttpServerException(method.getName() + " 最多只能有一个参数");

			if (method.getParameterCount() > 0)
			{
				Class<?> paramClazz = method.getParameterTypes()[0];
				if (!paramSet.contains(paramClazz))
					throw new HttpServerException(method.getName() + " 的参数只能是：" + paramString);
			}

			this.parseMethod(method, annoHttp);
		}

		clazzSet.add(clazz);
	}

	private void parseMethod(Method method, Http annoHttp)
	{
		Set<HttpMethod> httpMethodSet = new HashSet<>();
		this.addHttpMethod(httpMethodSet, annoHttp.method());

		Charset charset = annoHttp.charset();
		ContentType contentType = annoHttp.contentType();
		StringBuilder url = new StringBuilder(32);
		if (!annoHttp.url().startsWith(SPLIT))
		{
			url.append(SPLIT);
		}
		url.append(annoHttp.url());

		Http annoMethod = method.getAnnotation(Http.class);
		if (annoMethod != null) // 方法配置覆盖类配置
		{
			charset = annoMethod.charset();
			contentType = annoMethod.contentType();
			httpMethodSet.clear();
			this.addHttpMethod(httpMethodSet, annoMethod.method());
			if (!annoMethod.url().isEmpty() && !annoMethod.url().startsWith(SPLIT))
			{
				url.append(SPLIT);
			}
			url.append(annoMethod.url());
		}

		if (httpMethodSet.isEmpty()) // 没有关联httpMethod
		{
			httpMethodSet.addAll(methodSet);
		}
		for (HttpMethod httpMethod : httpMethodSet)
		{
			HttpUrlConfig config = new HttpUrlConfig(url.toString(), httpMethod);
			if (parserCache.containsConfig(config))
				throw new HttpServerException("url 已经存在: " + config);

			parserCache.putConfig(config, new HttpUrlMethod(charset, method, contentType));
			logger.info("Found Http: {} [{}] [{}] [{}]，{}.{}()", config.getUrl(), config.getMethod().name(),
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
