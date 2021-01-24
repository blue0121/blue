package test.http.controller;

import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.annotation.PathVariable;
import blue.http.annotation.QueryParam;
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

	@Http(url = "/hello3/{name}", method = HttpMethod.GET)
	public String hello3(@PathVariable("name") String name)
	{
		System.out.println(name);
		return "hello3, " + name;
	}

	@Http(url = "/hello4", method = HttpMethod.GET)
	public String hello4(@QueryParam("name") String name)
	{
		System.out.println(name);
		return "hello4, " + name;
	}

}
