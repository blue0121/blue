package test.http;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
@SpringJUnitConfig(locations = {"classpath:/spring/http.xml"})
public abstract class BaseTest
{
	public BaseTest()
	{
	}

}
