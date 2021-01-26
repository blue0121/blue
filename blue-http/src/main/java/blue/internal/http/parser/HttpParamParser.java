package blue.internal.http.parser;

import blue.http.annotation.BodyContent;
import blue.http.annotation.BodyJson;
import blue.http.annotation.BodyParam;
import blue.http.annotation.Multipart;
import blue.http.annotation.PathVariable;
import blue.http.annotation.QueryParam;
import blue.http.annotation.Validated;
import blue.internal.http.annotation.RequestParamConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-19
 */
public class HttpParamParser
{
	private static final Set<Class<?>> paramAnnotationSet = Set.of(BodyContent.class, BodyJson.class, BodyParam.class,
			Multipart.class, PathVariable.class, QueryParam.class);

	private HttpParamParser()
	{
	}

	public static List<RequestParamConfig> parse(Method method)
	{
		List<RequestParamConfig> configList = new ArrayList<>();
		for (var param : method.getParameters())
		{
			RequestParamConfig config = new RequestParamConfig();
			configList.add(config);
			config.setName(param.getName());
			config.setParamClazz(param.getType());
			for (var annotation : param.getAnnotations())
			{
				if (annotation instanceof Validated)
				{
					config.setValidAnnotation((Validated)annotation);
					continue;
				}
				if (!paramAnnotationSet.contains(annotation.annotationType()))
					continue;

				if (config.hasParamAnnotation())
					throw new IllegalArgumentException("Annotation is conflict at " + param.getName());

				config.setParamAnnotation(annotation);
			}
		}

		return configList;
	}

}
