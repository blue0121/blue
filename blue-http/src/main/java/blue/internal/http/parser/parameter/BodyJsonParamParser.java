package blue.internal.http.parser.parameter;

import blue.http.annotation.BodyJson;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class BodyJsonParamParser implements ParamParser<BodyJson>
{
	public BodyJsonParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, BodyJson annotation)
	{
		config.setParamAnnotationClazz(annotation.annotationType());
		config.setParamAnnotationValue(annotation.jsonPath());
		config.setParamAnnotationRequired(annotation.required());
	}
}
