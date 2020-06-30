package test.http.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * @author Jin Zheng
 * @since 2020-01-27
 */
public class FileNameMapTest
{
	private FileNameMap map;

	public FileNameMapTest()
	{
	}

	@BeforeEach
	public void before()
	{
		this.map = URLConnection.getFileNameMap();
	}

	@Test
	public void test()
	{
		String type1 =  map.getContentTypeFor("test.html");
		System.out.println(type1);
		Assertions.assertEquals("text/html", type1);

		String type2 =  map.getContentTypeFor("test.png");
		System.out.println(type2);
		Assertions.assertEquals("image/png", type2);

		String type3 =  map.getContentTypeFor("test.jpg");
		System.out.println(type3);
		Assertions.assertEquals("image/jpeg", type3);
	}

}
