package test.http;

import blue.http.HttpServer;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class HttpsMain
{
	private static String[] springs = {"/spring/https.xml"};

	public HttpsMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		HttpServer.run(springs);
	}

}
