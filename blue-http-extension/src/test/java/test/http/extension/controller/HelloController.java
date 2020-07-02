package test.http.extension.controller;

import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import org.springframework.stereotype.Controller;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
@Controller
@Http(url = "/hello", method = HttpMethod.POST)
public class HelloController
{
	public HelloController()
	{
	}

	public String index(String name)
	{
		return "hello, " + name;
	}

	@Http(url = "/hello2", method = HttpMethod.POST)
	public String hello2(String name)
	{
		return "hello2, " + name;
	}

}
