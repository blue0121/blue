package blue.internal.http.net;

import blue.http.HttpServer;
import blue.http.exception.HttpServerException;
import blue.internal.http.config.HttpConfig;
import blue.internal.http.config.SslConfig;
import blue.internal.http.config.WebSocketConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public class DefaultHttpServer implements HttpServer, InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(DefaultHttpServer.class);

	private int ioThread = Runtime.getRuntime().availableProcessors();
	private int port;
	private TaskExecutor taskExecutor;
	private HttpConfig httpConfig;
	private SslConfig sslConfig;
	private WebSocketConfig webSocketConfig;

	private HttpInitializer initializer;

	public DefaultHttpServer()
	{
	}

	@Override
	public void start() throws Exception
	{
		this.start(-1);
	}

	@Override
	public void start(long start) throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(ioThread);

		try
		{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(initializer)
					.option(ChannelOption.SO_BACKLOG, 1024);

			ChannelFuture channelFuture= bootstrap.bind(port).sync();
			if (start > 0)
			{
				logger.info("Http server started successful，listen port at {}，used {} ms.",
						port, System.currentTimeMillis() - start);
			}
			else
			{
				logger.info("Http server started successful，listen port at {}.", port);
			}
			channelFuture.channel().closeFuture().sync();
			logger.info("Http server closed.");
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (httpConfig == null && webSocketConfig == null)
			throw new HttpServerException("HttpConfig 和 WebsocketConfig 不能同时为空");

		if (taskExecutor == null)
			throw new HttpServerException("TaskExecutor 不能为空");

		this.initializer = new HttpInitializer(taskExecutor, httpConfig, sslConfig, webSocketConfig);
	}

	public void setIoThread(int ioThread)
	{
		this.ioThread = ioThread;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	public void setHttpConfig(HttpConfig httpConfig)
	{
		this.httpConfig = httpConfig;
	}

	public void setSslConfig(SslConfig sslConfig)
	{
		this.sslConfig = sslConfig;
	}

	public void setWebSocketConfig(WebSocketConfig webSocketConfig)
	{
		this.webSocketConfig = webSocketConfig;
	}

	public int getIoThread()
	{
		return ioThread;
	}

	public int getPort()
	{
		return port;
	}
}
