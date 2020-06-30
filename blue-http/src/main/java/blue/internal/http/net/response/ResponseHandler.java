package blue.internal.http.net.response;

import blue.http.message.Response;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 结果处理器
 *
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public interface ResponseHandler
{

	/**
	 * 是否接受该处理器来处理HTTP响应结果
	 *
	 * @param target 目标对象
	 * @return true表示接受
	 */
	boolean accepted(Object target);

	/**
	 * 处理HTTP响应结果
	 *
	 * @param ch Http 连接
	 * @param request Http 请求
	 * @param response 响应结果
	 */
	void handle(Channel ch, HttpRequest request, Response response);

}
