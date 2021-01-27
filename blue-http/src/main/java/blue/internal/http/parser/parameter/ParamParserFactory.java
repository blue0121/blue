package blue.internal.http.parser.parameter;

import blue.core.common.Singleton;
import blue.http.annotation.BodyContent;
import blue.http.annotation.BodyJson;
import blue.http.annotation.BodyParam;
import blue.http.annotation.Multipart;
import blue.http.annotation.PathVariable;
import blue.http.annotation.QueryParam;
import blue.http.annotation.Validated;
import blue.internal.http.annotation.RequestParamConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2021-01-26
 */
public class ParamParserFactory
{
	private static Logger logger = LoggerFactory.getLogger(ParamParserFactory.class);

	private static final Set<Class<?>> paramAnnotationSet = Set.of(BodyContent.class, BodyJson.class, BodyParam.class,
			Multipart.class, PathVariable.class, QueryParam.class);

	private Map<Class<?>, ParamParser> parserMap = new HashMap<>();

	public ParamParserFactory()
	{
		parserMap.put(BodyContent.class, new BodyContentParamParser());
		parserMap.put(BodyJson.class, new BodyJsonParamParser());
		parserMap.put(BodyParam.class, new BodyParamParamParser());
		parserMap.put(Multipart.class, new MultipartParamParser());
		parserMap.put(PathVariable.class, new PathVariableParamParser());
		parserMap.put(QueryParam.class, new QueryParamParamParser());
		parserMap.put(Validated.class, new ValidatedParamParser());
	}

	public static ParamParserFactory getInstance()
	{
		return Singleton.get(ParamParserFactory.class);
	}

	public List<RequestParamConfig> parse(Method method)
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
					this.parse(config, annotation);
					continue;
				}

				if (!paramAnnotationSet.contains(annotation.annotationType()))
					continue;

				if (config.hasParamAnnotation())
					throw new IllegalArgumentException("Annotation is conflict at " + param.getName());

				this.parse(config, annotation);
			}
		}

		return configList;
	}

	@SuppressWarnings("unchecked")
	public void parse(RequestParamConfig config, Annotation annotation)
	{
		ParamParser parser = parserMap.get(annotation.annotationType());
		if (parser == null)
		{
			logger.warn("ParamParser is null, annotation: {}", annotation.annotationType());
			return;
		}
		parser.parse(config, annotation);
	}

}
