package blue.internal.http.net;

import blue.internal.http.config.HttpConfig;
import blue.internal.http.config.SslConfig;
import blue.internal.http.config.WebSocketConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.core.task.TaskExecutor;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpInitializer extends ChannelInitializer<SocketChannel>
{
	private final TaskExecutor taskExecutor;
	private final HttpConfig httpConfig;
	private final SslConfig sslConfig;
	private final WebSocketConfig webSocketConfig;

	public HttpInitializer(TaskExecutor taskExecutor, HttpConfig httpConfig, SslConfig sslConfig, WebSocketConfig webSocketConfig)
	{
		this.taskExecutor = taskExecutor;
		this.httpConfig = httpConfig;
		this.sslConfig = sslConfig;
		this.webSocketConfig = webSocketConfig;
	}

	@Override
	protected void initChannel(SocketChannel sc) throws Exception
	{
		ChannelPipeline cp = sc.pipeline();

		if (sslConfig != null)
		{
			cp.addLast(sslConfig.getSslContext().newHandler(sc.alloc()));
		}

		cp.addLast(new HttpServerCodec());
		cp.addLast(new HttpServerKeepAliveHandler());

		if (httpConfig != null)
		{
			cp.addLast(new ChunkedWriteHandler());
			cp.addLast(new HttpServerHandler(taskExecutor, httpConfig, webSocketConfig));
		}

		if (webSocketConfig != null)
		{
			cp.addLast(new HttpObjectAggregator(Short.MAX_VALUE * 10));
			cp.addLast(new WebSocketServerProtocolHandler(webSocketConfig.getRoot(), null, true));
			cp.addLast(new WebSocketServerHandler(taskExecutor, webSocketConfig));
		}
	}


}
