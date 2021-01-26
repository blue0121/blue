package blue.internal.http.parser.parameter;

import blue.internal.http.annotation.RequestParamConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2021-01-26
 */
public class ParamParserFactory
{
	private static Logger logger = LoggerFactory.getLogger(ParamParserFactory.class);

	private Map<Class<?>, ParamParser> parserMap = new HashMap<>();

	public ParamParserFactory()
	{
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
