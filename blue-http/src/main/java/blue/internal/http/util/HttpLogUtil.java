package blue.internal.http.util;

import blue.http.exception.HttpErrorCodeException;
import blue.http.exception.HttpServerException;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * HTTP日志工具类
 * 
 * @author zhengj
 * @since 1.0 2016年10月10日
 */
public class HttpLogUtil
{
	private static Logger logger = LoggerFactory.getLogger(HttpLogUtil.class);

	private HttpLogUtil()
	{
	}

	public static void success(Request request, String content)
	{
		log(request, "Success", content);
	}

	public static void error(Request request, String content, Throwable cause)
	{
		log(request, "Error", content);
		if (cause != null)
		{
			if (cause.getClass() == HttpServerException.class || cause.getClass() == HttpErrorCodeException.class)
			{
				StackTraceElement[] elements = cause.getStackTrace();
				StackTraceElement element = elements.length > 0 ? elements[0] : null;
				logger.error("EXCEPTION ({}): {}, at {}", cause.getClass().getSimpleName(), cause.getMessage(), element);
			}
			else
			{
				logger.error("EXCEPTION: ", cause);
			}
		}
	}

	private static void log(Request request, String status, String content)
	{
		long used = System.currentTimeMillis() - request.getStart();
		StringBuilder sb = new StringBuilder(128);
		sb.append("url: ").append(request.getUrl());
		sb.append(", method: ").append(request.getHttpMethod().name());
		sb.append(", ").append(status);
		sb.append(", used: ").append(used).append("ms");

		Map<String, String> pathMap = request.getPathVariableMap();
		if (!pathMap.isEmpty())
		{
			sb.append("\nPATH: ").append(pathMap);
		}

		String get = request.getQueryString();
		if (get != null && !get.isEmpty())
		{
			sb.append("\nGET: ").append(get);
		}

		Map<String, String> postMap = request.getPostMap();
		if (!postMap.isEmpty())
		{
			sb.append("\nPOST: ").append(postMap);
		}
		Map<String, UploadFile> fileMap = request.getFileMap();
		if (!fileMap.isEmpty())
		{
			sb.append("\nUPLOAD: ").append(fileMap);
		}

		if (content != null && !content.isEmpty())
		{
			sb.append("\nRETURN: ").append(content);
		}
		logger.info(sb.toString());
	}
}
