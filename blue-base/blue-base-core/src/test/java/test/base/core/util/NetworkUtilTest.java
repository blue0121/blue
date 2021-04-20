package test.base.core.util;

import blue.base.core.util.NetworkUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author zhengjin
 * @since 1.0 2018年08月23日
 */
public class NetworkUtilTest
{
	public NetworkUtilTest()
	{
	}

	@Test
	public void testGetAllAddress()
	{
		Set<String> addrSet = NetworkUtil.getAllAddress();
		for (String addr : addrSet)
		{
			System.out.println(addr);
		}
	}

}
