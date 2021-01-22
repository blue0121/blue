package blue.core.common;

import com.alibaba.fastjson.JSONObject;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * ErrorCode 默认实现
 *
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class ErrorCode
{
	public static final int CODE_TO_STATUS = 1_000;

	public static final String CODE = "code";
	public static final String MESSAGE = "message";

	// 200
	public static final ErrorCode SUCCESS = new ErrorCode(200_000, "成功");

	// 400
	public static final ErrorCode INVALID_PARAM = new ErrorCode(400_000, "无效参数：{0}");
	public static final ErrorCode INVALID_JSON = new ErrorCode(400_001, "无效JSON格式");
	public static final ErrorCode NO_PARAM = new ErrorCode(400_002, "没有参数");
	public static final ErrorCode OUT_OF_MAX_UPLOAD_SIZE = new ErrorCode(400_003, "上传文件超出限制");
	public static final ErrorCode INVALID_UPLOAD_FILE = new ErrorCode(400_004, "无效文件上传");
	public static final ErrorCode EXISTS = new ErrorCode(400_004, "{0} 已经存在");
	public static final ErrorCode REQUIRED = new ErrorCode(400_004, "{0} 不能为空");

	// 401
	public static final ErrorCode LOGIN_FAILURE = new ErrorCode(401_000, "登录失败");
	public static final ErrorCode INVALID_TOKEN = new ErrorCode(401_001, "无效令牌");
	public static final ErrorCode INVALID_SESSION = new ErrorCode(401_002, "无效会话");
	public static final ErrorCode INVALID_CAPTCHA = new ErrorCode(401_003, "无效验证码");

	// 403
	public static final ErrorCode NOT_ACCESS = new ErrorCode(403_000, "无访问权限");

	// 404
	public static final ErrorCode NOT_FOUND = new ErrorCode(404_000, "找不到URL");
	public static final ErrorCode NO_DATA = new ErrorCode(404_001, "没有数据");
	public static final ErrorCode NOT_EXISTS = new ErrorCode(404_002, "{0} 不存在");

	// 500
	public static final ErrorCode ERROR = new ErrorCode(500_000, "系统错误");
	public static final ErrorCode SYS_ERROR = new ErrorCode(500_001, "发生错误：{}");

	private int code;
	private String message;
	private Set<Integer> codeSet = new HashSet<>();

	protected ErrorCode(int code, String message)
	{
		this.code = code;
		this.message = message;
		this.check();
	}

	private void check()
	{
		if (code < 200_000 || code > 600_000)
			throw new IllegalArgumentException("code 无效：" + code);

		if (codeSet.contains(code))
			throw new IllegalArgumentException("code 已经存在：" + code);

		int error = code & CODE_TO_STATUS;
		if (this.getClass() != ErrorCode.class && error < 100)
			throw new IllegalArgumentException("code 必须在 100 和 999 之间");

		codeSet.add(code);
	}

	public final int getCode()
	{
		return code;
	}

	public final String getMessage(Object...args)
	{
		return MessageFormat.format(message, args);
	}

	public final int getHttpStatus()
	{
		return code / CODE_TO_STATUS;
	}

	public final String toJsonString(Object...args)
	{
		JSONObject json = new JSONObject();
		json.put(CODE, getCode());
		json.put(MESSAGE, this.getMessage(args));
		return json.toJSONString();
	}

	public ErrorCodeException newException(Object... args)
	{
		return new ErrorCodeException(this, args);
	}

}
