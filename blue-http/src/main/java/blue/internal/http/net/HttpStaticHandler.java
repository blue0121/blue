package blue.internal.http.net;

import blue.core.file.FilePath;
import blue.http.annotation.ContentType;
import blue.http.message.Download;
import blue.internal.http.config.HttpConfig;
import blue.internal.http.util.HttpServerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public class HttpStaticHandler
{
	private static Logger logger = LoggerFactory.getLogger(HttpStaticHandler.class);
	public static final String CP = "cp:";

	private HttpStaticHandler()
	{
	}

	public static void handle(Channel ch, HttpRequest request, HttpConfig httpConfig, String url)
	{
		Map<String, String> pathMap = httpConfig.getPathMap();
		if (pathMap == null || pathMap.isEmpty() || request.method() != HttpMethod.GET)
		{
			HttpServerUtil.sendError(ch, HttpResponseStatus.NOT_FOUND);
			return;
		}

		File file = parseFile(pathMap, url);
		if (file == null)
		{
			HttpServerUtil.sendError(ch, HttpResponseStatus.NOT_FOUND);
			return;
		}
		if (!file.isFile())
		{
			HttpServerUtil.sendError(ch, HttpResponseStatus.FORBIDDEN);
			return;
		}

		String ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
		if (ifModifiedSince != null && !ifModifiedSince.isEmpty())
		{
			SimpleDateFormat dateFormatter = new SimpleDateFormat(HttpServerUtil.HTTP_DATE_FORMAT, Locale.US);
			try
			{
				Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);
				long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
				long fileLastModifiedSeconds = file.lastModified() / 1000;
				if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds)
				{
					HttpServerUtil.sendStatus(ch, HttpResponseStatus.NOT_MODIFIED);
					return;
				}
			}
			catch (Exception e)
			{
				HttpServerUtil.sendError(ch, HttpResponseStatus.INTERNAL_SERVER_ERROR);
				return;
			}
		}

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		HttpServerUtil.setDateAndCacheHeaders(response, file);
		downloadFile(ch, request, response, file, file.getName(), false);

	}

	public static void download(Channel ch, HttpRequest request, Download download)
	{
		if (download.type() == Download.Type.FILE)
		{
			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			downloadFile(ch, request, response, download.file(), download.filename(), download.isDownload());
			download.file().delete();
			logger.info("Delete temp file: {}", download.file().getPath());
		}
		else if (download.type() == Download.Type.MEMORY)
		{
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			if (HttpUtil.isKeepAlive(request))
			{
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			HttpUtil.setContentLength(response, download.memory().length);
			if (download.filename() != null && !download.filename().isEmpty())
			{
				HttpServerUtil.setContentType(response, download.filename());
			}
			else
			{
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getName());
			}
			ByteBuf buf = Unpooled.copiedBuffer(download.memory());
			response.content().writeBytes(buf);
			buf.release();
			ChannelFuture future = ch.writeAndFlush(response);
			if (!HttpUtil.isKeepAlive(request))
			{
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}

	}

	private static void downloadFile(Channel ch, HttpRequest request, HttpResponse response,
	                                 File file, String filename, boolean download)
	{
		HttpUtil.setContentLength(response, file.length());
		if (filename == null || filename.isEmpty())
		{
			filename = file.getName();
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getName());
		}
		else
		{
			HttpServerUtil.setContentType(response, filename);
		}

		if (download)
		{
			HttpServerUtil.setDownloadFilename(response, filename);
		}
		if (HttpUtil.isKeepAlive(request))
		{
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		ch.write(response);

		ChannelFuture future = null;
		try
		{
			future = ch.writeAndFlush(new HttpChunkedInput(new ChunkedFile(file)), ch.newProgressivePromise());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (future != null && !HttpUtil.isKeepAlive(request))
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static File parseFile(Map<String, String> pathMap, String url)
	{
		String root = null;
		String path = null;
		for (Map.Entry<String, String> entry : pathMap.entrySet())
		{
			if (!FilePath.SLASH.equals(entry.getKey()) && url.startsWith(entry.getKey()))
			{
				root = entry.getKey();
				path = entry.getValue();
				break;
			}
		}
		if (root == null)
		{
			path = pathMap.get(FilePath.SLASH);
			root = FilePath.SLASH;
		}
		if (path == null || path.isEmpty())
			return null;

		String filepath = null;
		if (path.startsWith(CP))
		{
			String p = path.substring(CP.length());
			String pp = p + url.substring(root.length());
			URL u = HttpStaticHandler.class.getResource(pp);
			if (u == null)
				return null;

			filepath = u.getFile();
		}
		else
		{
			filepath = path + url.substring(root.length());
		}
		if (filepath == null)
			return null;

		logger.debug("Resource file：{}", filepath);
		File file = new File(filepath);
		if (!file.exists())
			return null;

		return file;
	}

}
