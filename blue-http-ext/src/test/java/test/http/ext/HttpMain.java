package test.http.ext;

import blue.http.HttpServer;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class HttpMain
{
	private static String[] springs = {"/spring/http.xml"};

	public HttpMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		HttpServer.run(springs);
	}

}
