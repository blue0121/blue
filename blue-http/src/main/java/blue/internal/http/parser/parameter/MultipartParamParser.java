package blue.internal.http.parser.parameter;

import blue.http.annotation.Multipart;
import blue.http.message.UploadFile;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
public class MultipartParamParser implements ParamParser<Multipart>
{
	public MultipartParamParser()
	{
	}

	@Override
	public void parse(RequestParamConfig config, Multipart annotation)
	{
		if (!UploadFile.class.isAssignableFrom(config.getParamClazz()))
			throw new IllegalArgumentException("@Multipart param type must be UploadFile class, name: " + config.getName());

		config.setParamAnnotationClazz(annotation.annotationType());
		config.setParamAnnotationValue(annotation.value());
		config.setParamAnnotationRequired(annotation.required());
	}
}
