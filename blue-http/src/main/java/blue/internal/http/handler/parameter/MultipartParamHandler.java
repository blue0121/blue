package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
import blue.http.annotation.Multipart;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import blue.internal.http.annotation.RequestParamConfig;

/**
 * @author Jin Zheng
 * @since 2021-01-24
 */
public class MultipartParamHandler implements ParamHandler<Request>
{
	public MultipartParamHandler()
	{
	}

	@Override
	public Object handle(RequestParamConfig config, Request request)
	{
		Multipart annotation = (Multipart) config.getParamAnnotation();
		UploadFile param = request.getFile(annotation.value());
		if (param == null && annotation.required())
			throw ErrorCode.REQUIRED.newException(config.getName());

		return param;
	}
}
