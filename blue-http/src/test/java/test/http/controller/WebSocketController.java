package test.http.controller;

import blue.http.annotation.WebSocket;
import blue.http.message.WebSocketRequest;
import org.springframework.stereotype.Controller;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
@Controller
@WebSocket(url = "/hello")
public class WebSocketController
{
	public WebSocketController()
	{
	}

	public String index(WebSocketRequest request)
	{
		String name = request.getObject(String.class);
		System.out.println(name);
		return "hello, " + name;
	}

}
