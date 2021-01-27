package blue.internal.http.parser.parameter;

import blue.http.annotation.BodyParam;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class BodyParamParamParser implements ParamParser<BodyParam>
{
	public BodyParamParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, BodyParam annotation)
	{
		config.setParamAnnotationClazz(annotation.annotationType());
		config.setParamAnnotationValue(annotation.value());
		config.setParamAnnotationRequired(annotation.required());
	}
}
