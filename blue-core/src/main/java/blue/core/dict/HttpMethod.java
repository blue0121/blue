package blue.core.dict;

/**
 * HTTP 方法
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class HttpMethod extends Dictionary
{
	public static final HttpMethod GET = new HttpMethod(1, "GET", Color.BLUE);
	public static final HttpMethod POST = new HttpMethod(2, "POST", Color.RED);
	public static final HttpMethod PUT = new HttpMethod(3, "PUT", Color.MAROON);
	public static final HttpMethod DELETE = new HttpMethod(4, "DELETE", Color.PURPLE);
	public static final HttpMethod PATCH = new HttpMethod(4, "PATCH", Color.AQUA);
	public static final HttpMethod HEAD = new HttpMethod(5, "HEAD", Color.GRAY);
	public static final HttpMethod TRACE = new HttpMethod(6, "TRACE", Color.NAVY);
	public static final HttpMethod OPTIONS = new HttpMethod(7, "OPTIONS", Color.TEAL);
	public static final HttpMethod CONNECT = new HttpMethod(8, "CONNECT", Color.FUCHSIA);
	
	private HttpMethod(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
