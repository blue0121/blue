package test.http.extension.controller;

import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.message.View;
import org.springframework.stereotype.Controller;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-19
 */
@Controller
@Http(url = "/ftl", method = HttpMethod.GET)
public class FtlController
{
	public FtlController()
	{
	}


	public View index()
	{
		return View.createBuilder().setView("index").put("name", "blue").build();
	}

}
