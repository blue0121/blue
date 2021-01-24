package blue.internal.http.parser;

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
public class WebSocketParamParser
{
	private static final Set<Class<?>> paramAnnotationSet = Set.of();

	private WebSocketParamParser()
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
			config.setParam(param);
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
