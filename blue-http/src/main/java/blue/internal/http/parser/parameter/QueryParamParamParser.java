package blue.internal.http.parser.parameter;

import blue.http.annotation.QueryParam;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class QueryParamParamParser implements ParamParser<QueryParam>
{
	public QueryParamParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, QueryParam annotation)
	{
		config.setParamAnnotationClazz(annotation.annotationType());
		config.setParamAnnotationValue(annotation.value());
		config.setParamAnnotationRequired(annotation.required());
	}
}
