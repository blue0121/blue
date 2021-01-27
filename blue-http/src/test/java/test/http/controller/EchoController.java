package test.http.controller;

import blue.core.util.JsonUtil;
import blue.http.annotation.BodyJson;
import blue.http.annotation.Http;
import blue.http.annotation.HttpMethod;
import blue.http.annotation.Validated;
import org.springframework.stereotype.Controller;
import test.http.model.User;

import javax.validation.ValidationException;


/**
 * @author Jin Zheng
 * @since 1.0 2020-01-02
 */
@Controller
@Http(url = "/echo")
public class EchoController
{
	public EchoController()
	{
	}

	public String echo(String text)
	{
		if (text == null || text.isEmpty())
			throw new ValidationException("text is empty");

		return text;
	}

	@Http(url = "/validate", method = HttpMethod.POST)
	public void validate(@BodyJson(jsonPath = "$.user") @Validated User user)
	{
		System.out.println(JsonUtil.output(user));
		System.out.println("OK");
	}

}
