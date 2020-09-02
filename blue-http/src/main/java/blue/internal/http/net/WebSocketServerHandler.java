package blue.internal.http.net;

import blue.core.common.ErrorCode;
import blue.http.message.WebSocketRequest;
import blue.http.message.WebSocketResponse;
import blue.internal.http.config.WebSocketConfig;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.handler.HandlerFactory;
import blue.internal.http.mapping.HandlerMappingFactory;
import blue.internal.http.message.DefaultWebSocketRequest;
import blue.internal.http.message.WebSocketMessage;
import blue.internal.http.util.WebSocketServerUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

/**
 * @author Jin Zheng
 * @since 2020-02-03
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{
	private static Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

	private final TaskExecutor taskExecutor;
	private final WebSocketConfig webSocketConfig;

	public WebSocketServerHandler(TaskExecutor taskExecutor, WebSocketConfig webSocketConfig)
	{
		this.taskExecutor = taskExecutor;
		this.webSocketConfig = webSocketConfig;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		Channel ch = ctx.channel();
		WebSocketMessage message = WebSocketServerUtil.getWebSocketMessage(ch, frame.text());
		if (message == null)
			return;

		WebSocketRequest request = DefaultWebSocketRequest.from(message);
		HandlerChain chain = HandlerMappingFactory.getInstance().getHandlerChain(request);
		if (chain == null)
		{
			WebSocketServerUtil.sendText(ch, message, null, ErrorCode.NOT_FOUND);
			return;
		}
		taskExecutor.execute(() ->
		{
			WebSocketResponse response = HandlerFactory.getInstance().handle(request, chain);
			WebSocketServerUtil.sendText(ch, message, response);
		});

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelInactive(ctx);
		Channel ch = ctx.channel();
		//router.remove(ch);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		super.exceptionCaught(ctx, cause);
		Channel ch = ctx.channel();
		ChannelId id = ch.id();
		logger.error("WebSocket client raised exception，disconnected: " + ch.remoteAddress() + "，id=" + id, cause);
		ch.close();
	}

}
