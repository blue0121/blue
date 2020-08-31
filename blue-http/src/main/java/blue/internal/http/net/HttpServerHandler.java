package blue.internal.http.net;

import blue.http.annotation.HttpMethod;
import blue.http.message.Response;
import blue.http.message.UploadFile;
import blue.internal.http.config.HttpConfig;
import blue.internal.http.config.WebSocketConfig;
import blue.internal.http.handler.HandlerChain;
import blue.internal.http.handler.HandlerFactory;
import blue.internal.http.mapping.HandlerMappingFactory;
import blue.internal.http.message.DefaultRequest;
import blue.internal.http.message.DefaultUploadFile;
import blue.internal.http.net.response.ResponseHandlerFactory;
import blue.internal.http.parser.HttpMethodResult;
import blue.internal.http.util.HttpServerUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{
	private final static HttpDataFactory FACTORY = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
	private static Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	private final TaskExecutor taskExecutor;
	private final HttpConfig httpConfig;
	private final WebSocketConfig webSocketConfig;

	private Channel channel;
	private HttpRequest httpRequest;
	private DefaultRequest request;
	private HandlerChain chain;
	private StringBuilder contentBuilder = new StringBuilder();
	private Charset charset;
	private boolean isText = true;
	private boolean isHttp = true;
	private HttpPostRequestDecoder decoder;

	public HttpServerHandler(TaskExecutor taskExecutor, HttpConfig httpConfig, WebSocketConfig webSocketConfig)
	{
		this.taskExecutor = taskExecutor;
		this.httpConfig = httpConfig;
		this.webSocketConfig = webSocketConfig;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
	{
		this.channel = ctx.channel();
		if (msg instanceof HttpRequest)
		{
			this.httpRequest = (HttpRequest) msg;
			this.handleRequest(ctx);
		}
		else if (msg instanceof HttpContent)
		{
			this.handleContent(ctx, (HttpContent) msg);
		}
	}

	private void handleRequest(ChannelHandlerContext ctx)
	{
		HttpMethod httpMethod = HttpServerUtil.getHttpMethod(ctx, httpRequest,
				webSocketConfig == null ? null : webSocketConfig.getRoot(), httpConfig.getMaxUploadSize());
		if (httpMethod == null)
		{
			this.isHttp = false;
			return;
		}

		isText = HttpServerUtil.isPostText(httpRequest);
		String ip = HttpServerUtil.getIp(httpRequest.headers(), ctx.channel());
		request = new DefaultRequest(httpMethod, ip, channel.id());
		request.setContentLength(HttpUtil.getContentLength(httpRequest, 0L));
		request.parseUri(httpRequest.uri(), httpConfig.getPath());
		request.parseHeaders(httpRequest.headers());
		chain = HandlerMappingFactory.getInstance().getHandlerChain(request);
		if (chain == null)
		{
			taskExecutor.execute(() -> HttpStaticHandler.handle(channel, httpRequest, httpConfig, request.getUrl()));
			return;
		}
		HttpMethodResult result = (HttpMethodResult) chain.getHandler();
		request.putPath(result.getPathMap());
		charset = Charset.forName(result.getCharset().getName());
		decoder = new HttpPostRequestDecoder(FACTORY, httpRequest);
	}

	private void handleContent(ChannelHandlerContext ctx, HttpContent content)
	{
		if (!isHttp || httpRequest == null)
		{
			ctx.fireChannelRead(content.retain());
			return;
		}

		if (isText && request.getHttpMethod() == HttpMethod.POST)
		{
			String c = charset == null ? content.content().toString() : content.content().toString(charset);
			contentBuilder.append(c);
		}

		if (decoder == null)
			return;

		decoder.offer(content);
		try
		{
			while (decoder.hasNext())
			{
				InterfaceHttpData data = decoder.next();
				if (data != null)
				{
					String name = data.getName();
					if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
					{
						FileUpload fileUpload = (FileUpload)data;
						if (fileUpload.isCompleted())
						{
							UploadFile uploadFile = new DefaultUploadFile(fileUpload);
							request.putFile(name, uploadFile);
							logger.info("File upload successful: {}", data);
						}
					}
					else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute
							|| data.getHttpDataType() == InterfaceHttpData.HttpDataType.InternalAttribute)
					{
						Attribute attribute = (Attribute)data;
						String value = attribute.getString();
						request.putPost(name, value);
					}
				}
			}
		}
		catch (HttpPostRequestDecoder.EndOfDataDecoderException e)
		{
			logger.debug("End of data decode");
		}
		catch (IOException e)
		{
			logger.warn("File upload raised exception, ", e);
		}

		if (content instanceof LastHttpContent)
		{
			request.setContent(contentBuilder.toString());
			taskExecutor.execute(() ->
			{
				Response response = HandlerFactory.getInstance().handle(request, chain);
				ResponseHandlerFactory.getInstance().handle(channel, httpRequest, response);
				decoder.destroy();
				decoder = null;
			});
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		super.exceptionCaught(ctx, cause);
		Channel ch = ctx.channel();
		ChannelId id = ch.id();
		logger.error("Http client raised exception，disconnected: " + ch.remoteAddress() + "，id=" + id, cause);
		if (ctx.channel().isActive())
		{
			HttpServerUtil.sendError(ch, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
