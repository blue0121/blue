package blue.internal.http.handler.parameter;

import blue.core.common.ErrorCode;
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
		UploadFile param = request.getFile(config.getParamAnnotationValue());
		if (param == null && config.isParamAnnotationRequired())
			throw ErrorCode.REQUIRED.newException(config.getName());

		return param;
	}
}
