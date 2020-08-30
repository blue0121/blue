package blue.http.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 错误代码
 *
 * @author Jin Zheng
 * @since 2020-01-27
 */
public interface ErrorCode
{
	int CODE_TO_STATUS = 1_000;

	String CODE = "code";
	String MESSAGE = "message";

	/**
	 * 错误代码 4xx_1xx
	 * @return
	 */
	int getCode();

	/**
	 * 错误信息，可包含{x}占位符，用MessageFormat实现
	 * @param args 参数
	 * @return
	 */
	String getMessage(Object...args);

	/**
	 * 根据 getCode() 计算出对应的 HttpStatus
	 * @return
	 */
	HttpResponseStatus getHttpStatus();

	/**
	 * 转化为JSON字符串
	 * @param args 占位符
	 * @return
	 */
	String toJsonString(Object...args);

	/**
	 * 创建异常
	 * @param args
	 * @return
	 */
	HttpErrorCodeException newException(Object...args);

}
