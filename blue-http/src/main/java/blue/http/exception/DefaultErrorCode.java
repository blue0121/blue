package blue.http.exception;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * ErrorCode 默认实现
 *
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class DefaultErrorCode implements ErrorCode
{
	// 200
	public static final ErrorCode SUCCESS = new DefaultErrorCode(200_000, "成功");

	// 400
	public static final ErrorCode INVALID_PARAM = new DefaultErrorCode(400_000, "无效参数：{0}");
	public static final ErrorCode INVALID_JSON = new DefaultErrorCode(400_001, "无效JSON格式");
	public static final ErrorCode NO_PARAM = new DefaultErrorCode(400_002, "没有参数");
	public static final ErrorCode OUT_OF_MAX_UPLOAD_SIZE = new DefaultErrorCode(400_003, "上传文件超出限制");
	public static final ErrorCode INVALID_UPLOAD_FILE = new DefaultErrorCode(400_004, "无效文件上传");
	public static final ErrorCode EXISTS = new DefaultErrorCode(400_004, "{0} 已经存在");

	// 401
	public static final ErrorCode LOGIN_FAILURE = new DefaultErrorCode(401_000, "登录失败");
	public static final ErrorCode INVALID_TOKEN = new DefaultErrorCode(401_001, "无效令牌");
	public static final ErrorCode INVALID_SESSION = new DefaultErrorCode(401_002, "无效会话");
	public static final ErrorCode INVALID_CAPTCHA = new DefaultErrorCode(401_003, "无效验证码");
	public static final ErrorCode NOT_ACCESS = new DefaultErrorCode(401_004, "无访问权限");

	// 404
	public static final ErrorCode NOT_FOUND = new DefaultErrorCode(404_000, "找不到URL");
	public static final ErrorCode NO_DATA = new DefaultErrorCode(404_001, "没有数据");
	public static final ErrorCode NOT_EXISTS = new DefaultErrorCode(404_002, "{0} 不存在");

	// 500
	public static final ErrorCode ERROR = new DefaultErrorCode(500_000, "系统错误");
	public static final ErrorCode SYS_ERROR = new DefaultErrorCode(500_001, "发生错误：{}");

	private int code;
	private String message;
	private Set<Integer> codeSet = new HashSet<>();

	protected DefaultErrorCode(int code, String message)
	{
		this.code = code;
		this.message = message;
		this.check();
	}

	private void check()
	{
		if (code < 200_000 || code > 600_000)
			throw new HttpServerException("code 无效：" + code);

		if (codeSet.contains(code))
			throw new HttpServerException("code 已经存在：" + code);

		int error = code & CODE_TO_STATUS;
		if (this.getClass() != DefaultErrorCode.class && error < 100)
			throw new HttpServerException("code 必须在 100 和 999 之间");

		codeSet.add(code);
	}

	@Override
	public final int getCode()
	{
		return code;
	}

	@Override
	public final String getMessage(Object...args)
	{
		return MessageFormat.format(message, args);
	}

	@Override
	public final HttpResponseStatus getHttpStatus()
	{
		int status = this.getCode() / CODE_TO_STATUS;
		return HttpResponseStatus.valueOf(status);
	}

	@Override
	public final String toJsonString(Object...args)
	{
		JSONObject json = new JSONObject();
		json.put(CODE, getCode());
		json.put(MESSAGE, this.getMessage(args));
		return json.toJSONString();
	}

	@Override
	public HttpErrorCodeException newException(Object... args)
	{
		return new HttpErrorCodeException(this, args);
	}

}
