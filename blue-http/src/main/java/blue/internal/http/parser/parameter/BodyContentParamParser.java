package blue.internal.http.parser.parameter;

import blue.http.annotation.BodyContent;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class BodyContentParamParser implements ParamParser<BodyContent>
{
	public BodyContentParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, BodyContent annotation)
	{
		config.setParamAnnotationClazz(annotation.annotationType());
	}
}
