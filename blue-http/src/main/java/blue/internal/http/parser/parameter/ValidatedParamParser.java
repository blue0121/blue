package blue.internal.http.parser.parameter;

import blue.http.annotation.Validated;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class ValidatedParamParser implements ParamParser<Validated>
{
	public ValidatedParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, Validated annotation)
	{
		config.setValidated(true);
		config.setValidatedGroups(annotation.value());
	}
}
