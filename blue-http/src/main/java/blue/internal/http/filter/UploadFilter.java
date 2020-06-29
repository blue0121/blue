package blue.internal.http.filter;

import blue.http.filter.HttpFilter;
import blue.http.message.Request;
import blue.http.message.Response;
import blue.http.message.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-08
 */
public class UploadFilter implements HttpFilter
{
	private static Logger logger = LoggerFactory.getLogger(UploadFilter.class);

	public UploadFilter()
	{
	}

	@Override
	public void afterCompletion(Request request, Response response, Exception ex) throws Exception
	{
		Map<String, UploadFile> fileMap = request.getFileMap();
		if (fileMap == null || fileMap.isEmpty())
			return;

		for (Map.Entry<String, UploadFile> entry : fileMap.entrySet())
		{
			UploadFile uploadFile = entry.getValue();
			if (uploadFile == null)
				continue;

			File file = uploadFile.getFile();
			if (file == null)
				continue;

			if (!file.exists())
				continue;

			boolean result = file.delete();
			String strResult = result ? "Success" : "Failure";
			logger.info("delete file: {} - {}", strResult, uploadFile);
		}
	}
}
