package blue.internal.http.parser.parameter;

import blue.http.annotation.PathVariable;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class PathVariableParamParser implements ParamParser<PathVariable>
{
	public PathVariableParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, PathVariable annotation)
	{
		config.setParamAnnotationClazz(annotation.annotationType());
		config.setParamAnnotationValue(annotation.value());
		config.setParamAnnotationRequired(true);
	}
}
