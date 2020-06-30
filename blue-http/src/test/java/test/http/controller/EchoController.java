package test.http.controller;

import blue.http.annotation.Http;
import org.springframework.stereotype.Controller;

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

}
