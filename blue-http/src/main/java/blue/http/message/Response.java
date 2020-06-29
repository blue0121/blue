package blue.http.message;

import blue.http.annotation.Charset;
import blue.http.annotation.ContentType;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

/**
 * HTTP 请求响应
 *
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public interface Response
{
	/**
	 * 编码
	 *
	 * @return
	 */
	Charset getCharset();

	/**
	 * HTTP content-type
	 *
	 * @return
	 */
	ContentType getContentType();

	/**
	 * get Http Status
	 * @return
	 */
	HttpResponseStatus getHttpStatus();

	/**
	 * set Http Status
	 * @param httpStatus
	 */
	void setHttpStatus(HttpResponseStatus httpStatus);

	/**
	 * 调用结果
	 *
	 * @return
	 */
	Object getResult();

	/**
	 * 调用结果
	 *
	 * @param result
	 */
	void setResult(Object result);

	/**
	 * 获取错误
	 *
	 * @return
	 */
	Throwable getCause();

	/**
	 * 获取所有 Cookie
	 *
	 * @return
	 */
	Map<String, String> getCookie();

	/**
	 * 获取所有参数
	 * @return
	 */
	Map<String, Object> getParam();

}
