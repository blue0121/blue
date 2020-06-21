package blue.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网络工具类
 * 
 * @author zhengj
 * @since 1.0 2016年10月14日
 */
public class NetworkUtil
{
	private NetworkUtil()
	{
	}

	/**
	 * 获取所有IP地址
	 */
	public static Set<String> getAllAddress()
	{
		Set<String> addrSet = new HashSet<>();
		try
		{
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements())
				{
					InetAddress inetAddress = inetAddresses.nextElement();
					if (!(inetAddress instanceof Inet4Address))
						continue;

					addrSet.add(inetAddress.getHostAddress());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return addrSet;
	}
	
	public static List<String> getLocalAddress()
	{
		List<String> addrList = new ArrayList<>();
		try
		{
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements())
				{
					InetAddress inetAddress = inetAddresses.nextElement();
					if (!(inetAddress instanceof Inet4Address))
						continue;

					if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress())
						continue;
					
					addrList.add(inetAddress.getHostAddress());
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return addrList;
	}

	public static String getAddress()
	{
		List<String> addressList = getLocalAddress();
		return addressList.isEmpty() ? null : addressList.get(0);
	}
	
}
