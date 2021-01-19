package blue.internal.http.parser;

import blue.http.annotation.BodyContent;
import blue.http.annotation.BodyJson;
import blue.http.annotation.BodyParam;
import blue.http.annotation.Multipart;
import blue.http.annotation.PathVariable;
import blue.http.annotation.QueryParam;
import blue.http.annotation.Validated;
import blue.http.message.UploadFile;
import blue.internal.http.annotation.HttpUrlParamConfig;

import java.lang.annotation.Annotation;
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
	private static Set<Class<?>> paramAnnotationSet = Set.of(BodyContent.class, BodyJson.class, BodyParam.class,
			Multipart.class, PathVariable.class, QueryParam.class);

	private HttpParamParser()
	{
	}

	public static List<HttpUrlParamConfig> parse(Method method)
	{
		List<HttpUrlParamConfig> configList = new ArrayList<>();
		for (var param : method.getParameters())
		{
			HttpUrlParamConfig config = new HttpUrlParamConfig();
			configList.add(config);
			config.setName(param.getName());
			config.setParameter(param);
			config.setParamClazz(param.getType());
			for (var annotation : param.getAnnotations())
			{
				if (annotation instanceof Validated)
				{
					config.setValidated(true);
					config.setValidatedGroups(((Validated)annotation).value());
					continue;
				}

				if (paramAnnotationSet.contains(annotation.annotationType()) && config.hasParamAnnotation())
					throw new IllegalArgumentException("Annotation is conflict at " + param.getName());

				parseAnnotation(config, annotation);
			}
		}

		return configList;
	}

	private static void parseAnnotation(HttpUrlParamConfig config, Annotation annotation)
	{
		config.setParamAnnotation(annotation);
		if (annotation instanceof BodyContent)
		{
			// nothing
		}
		else if (annotation instanceof BodyJson)
		{
			BodyJson anno = (BodyJson) annotation;
			config.setValue(anno.jsonPath());
			config.setRequired(anno.required());
		}
		else if (annotation instanceof BodyParam)
		{
			BodyParam anno = (BodyParam) annotation;
			config.setValue(anno.value());
			config.setRequired(anno.required());
		}
		else if (annotation instanceof Multipart)
		{
			if (!UploadFile.class.isAssignableFrom(config.getParamClazz()))
				throw new IllegalArgumentException("@Multipart parameter must be UploadFile: " + config.getName());

			Multipart anno = (Multipart) annotation;
			config.setValue(anno.value());
			config.setRequired(anno.required());
		}
		else if (annotation instanceof PathVariable)
		{
			PathVariable anno = (PathVariable) annotation;
			config.setValue(anno.value());
			config.setRequired(true);
		}
		else if (annotation instanceof QueryParam)
		{
			QueryParam anno = (QueryParam) annotation;
			config.setValue(anno.value());
			config.setRequired(anno.required());
		}
	}

}
