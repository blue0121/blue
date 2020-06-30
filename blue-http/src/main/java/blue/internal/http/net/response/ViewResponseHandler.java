package blue.internal.http.net.response;

import blue.http.message.Response;
import blue.http.message.View;
import blue.internal.http.config.FreeMarkerConfig;
import blue.internal.http.util.HttpServerUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public class ViewResponseHandler implements ResponseHandler
{
	public ViewResponseHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return target != null && target instanceof View;
	}

	@Override
	public void handle(Channel ch, HttpRequest request, Response response)
	{
		View view = (View) response.getResult();
		if (view.location() != null && !view.location().isEmpty()) // 跳转页面
		{
			HttpServerUtil.sendRedirect(ch, view.location());
		}
		else // 渲染页面
		{
			String html = FreeMarkerConfig.getInstance().render(view.view(), view.model());
			if (html == null || html.isEmpty())
			{
				HttpServerUtil.sendError(ch, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				return;
			}

			response.setResult(html);
			HttpServerUtil.sendText(ch, request, response, true);
		}
	}
}
