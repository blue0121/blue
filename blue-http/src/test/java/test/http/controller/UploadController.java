package test.http.controller;

import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Jin Zheng
 * @since 1.0 2020-10-29
 */
@Controller
@Http(url = "/upload", method = HttpMethod.POST)
public class UploadController
{
	public UploadController()
	{
	}

	public void index(Request request)
	{
		System.out.println(request.getPostMap());

		UploadFile file = request.getFile();
		if (file == null)
		{
			System.out.println("Upload file is null");
			return;
		}
		System.out.printf("Upload file, name: %s, size: %s, type: %s\n",
				file.getOriginalName(), file.getSize(), file.getContentType());

		Path path = Paths.get("/opt/web/1.rar");
		try (InputStream is = file.getInputStream())
		{
			Files.copy(is, path);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
