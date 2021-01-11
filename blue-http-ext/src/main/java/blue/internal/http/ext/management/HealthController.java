package blue.internal.http.ext.management;

import blue.http.annotation.ContentType;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import org.springframework.stereotype.Controller;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-11
 */
@Controller
@Http(url = "/management/health", method = HttpMethod.GET, contentType = ContentType.JSON)
public class HealthController
{
	public HealthController()
	{
	}

	public String index()
	{
		String json = "{\"health\": \"up\"}";
		return json;
	}

}
