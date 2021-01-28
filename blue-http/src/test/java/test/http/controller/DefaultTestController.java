package test.http.controller;

import org.springframework.stereotype.Controller;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-27
 */
@Controller
public class DefaultTestController implements TestController
{
	public DefaultTestController()
	{
	}

	@Override
	public String test(String name)
	{
		return "test, " + name;
	}
}
