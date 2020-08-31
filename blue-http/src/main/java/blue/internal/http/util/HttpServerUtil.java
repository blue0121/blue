package blue.internal.http.util;

import blue.core.util.StringUtil;
import blue.http.annotation.ContentType;
import blue.http.annotation.HttpMethod;
import blue.http.message.Response;
import blue.internal.http.annotation.HttpConfigCache;
import blue.internal.http.parser.HttpParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Jin Zheng
 * @since 2020-01-12
 */
public class HttpServerUtil
{
	public static final int COOKIE_AGE = 3600 * 24 * 7;
	public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	public static final int HTTP_CACHE_SECONDS = 60;

	private static String errorTpl;

	static
	{
		try (InputStream is = HttpServerUtil.class.getResourceAsStream("/html/error.html"))
		{
			errorTpl = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private HttpServerUtil()
	{
	}

	public static void sendText(Channel ch, HttpRequest request, Response message, boolean isKeepAlive)
	{
		HttpResponseStatus httpStatus = message.getHttpStatus();
		if (httpStatus == null)
		{
			httpStatus = HttpResponseStatus.OK;
		}
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpStatus);
		ContentType contentType = message.getContentType();
		if (contentType == null || contentType == ContentType.AUTO)
		{
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getName());
		}
		else
		{
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType.getName());
		}
		isKeepAlive = isKeepAlive && HttpUtil.isKeepAlive(request);
		if (isKeepAlive)
		{
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		setCookie(response, message);
		String content = message.getResult().toString();
		ByteBuf buf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
		response.content().writeBytes(buf);
		buf.release();
		ChannelFuture future = ch.writeAndFlush(response);
		if (!isKeepAlive)
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	public static HttpMethod getHttpMethod(ChannelHandlerContext ctx, HttpRequest request,
	                                       String wsRoot, long maxUploadSize)
	{
		String uri = request.uri();
		if (wsRoot != null && !wsRoot.isEmpty() && uri.startsWith(wsRoot))
		{
			ctx.fireChannelRead(request);
			return null;
		}

		if (HttpUtil.is100ContinueExpected(request))
		{
			HttpServerUtil.sendStatus(ctx.channel(), HttpResponseStatus.CONTINUE);
			return null;
		}

		if (!request.decoderResult().isSuccess())
		{
			HttpServerUtil.sendError(ctx.channel(), HttpResponseStatus.BAD_REQUEST);
			return null;
		}

		HttpMethod httpMethod = HttpMethod.valueOf(request.method().name());
		if (httpMethod == HttpMethod.POST)
		{
			long contentLength = HttpUtil.getContentLength(request, 0L);
			if (maxUploadSize > 0 && maxUploadSize < contentLength)
			{
				HttpServerUtil.sendError(ctx.channel(), HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE);
				return null;
			}
		}
		return httpMethod;
	}

	public static void sendRedirect(Channel ch, String uri)
	{
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaderNames.LOCATION, uri);
		ch.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void sendError(Channel ch, HttpResponseStatus status)
	{
		String ver = "4.1.42 Final";
		Version version = Version.identify().get("netty-common");
		if (version != null)
			ver = version.artifactVersion();

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.HTML.getName());

		Map<String, String> param = new HashMap<>();
		param.put("status", status.toString());
		param.put("ver", ver);
		String error = StringUtil.template(errorTpl, param, HttpConfigCache.BRACE_START, HttpConfigCache.BRACE_END);
		ByteBuf buf = Unpooled.copiedBuffer(error, CharsetUtil.UTF_8);
		response.content().writeBytes(buf);
		buf.release();
		ch.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void sendStatus(Channel ch, HttpResponseStatus status)
	{
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		if (status == HttpResponseStatus.NOT_MODIFIED)
		{
			SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
			dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
			Calendar time = new GregorianCalendar();
			response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
		}

		ch.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void setCookie(HttpResponse response, Response message)
	{
		if (message.getCookie() == null || message.getCookie().isEmpty())
			return;

		ServerCookieEncoder encoder = ServerCookieEncoder.STRICT;
		HttpHeaders headers = response.headers();
		for (Map.Entry<String, String> entry : message.getCookie().entrySet())
		{
			Cookie cookie = new DefaultCookie(entry.getKey(), entry.getValue());
			cookie.setPath(HttpParser.SPLIT);
			cookie.setMaxAge(COOKIE_AGE);
			cookie.setHttpOnly(false);
			String strCookie = encoder.encode(cookie);
			headers.add(HttpHeaderNames.SET_COOKIE, strCookie);
		}
	}

	public static void setDateAndCacheHeaders(HttpResponse response, File file)
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		Calendar time = new GregorianCalendar();
		response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));

		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
		response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
		response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
		response.headers().set(HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(file.lastModified())));
	}

	public static void setContentType(HttpResponse response, String filename)
	{
		FileNameMap map = URLConnection.getFileNameMap();
		String mimeType = map.getContentTypeFor(filename);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeType);
	}

	public static void setDownloadFilename(HttpResponse response, String filename)
	{
		String name = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
		response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=" + name);
	}

	public static String getIp(HttpHeaders headers, Channel channel)
	{
		String ip = headers.get("x-forwarded-for");
		if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown"))
		{
			ip = headers.get("Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown"))
		{
			ip = headers.get("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown"))
		{
			InetSocketAddress address = (InetSocketAddress)channel.remoteAddress();
			ip = address.getAddress().getHostAddress();
		}
		return ip;
	}

	/**
	 * 是否文本提交
	 *
	 * @param request
	 * @return
	 */
	public static boolean isPostText(HttpRequest request)
	{
		CharSequence type = HttpUtil.getMimeType(request);
		if (type == null || type.length() == 0)
			return true;

		String mimeType = type.toString();
		if ("application/x-www-form-urlencoded".equals(mimeType))
			return true;
		if ("application/json".equals(mimeType))
			return true;
		if (mimeType.startsWith("text/"))
			return true;

		return false;
	}

}
