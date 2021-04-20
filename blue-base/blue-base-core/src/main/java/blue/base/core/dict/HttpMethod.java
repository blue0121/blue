package blue.base.core.dict;

/**
 * HTTP 方法
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class HttpMethod extends Dictionary {
	public static final HttpMethod GET = new HttpMethod(1, "GET", Color.PRIMARY);
	public static final HttpMethod POST = new HttpMethod(2, "POST", Color.DANGER);
	public static final HttpMethod PUT = new HttpMethod(3, "PUT", Color.WARNING);
	public static final HttpMethod DELETE = new HttpMethod(4, "DELETE", Color.INFO);
	public static final HttpMethod PATCH = new HttpMethod(4, "PATCH", Color.SUCCESS);
	public static final HttpMethod HEAD = new HttpMethod(5, "HEAD", Color.PRIMARY);
	public static final HttpMethod TRACE = new HttpMethod(6, "TRACE", Color.DANGER);
	public static final HttpMethod OPTIONS = new HttpMethod(7, "OPTIONS", Color.WARNING);
	public static final HttpMethod CONNECT = new HttpMethod(8, "CONNECT", Color.INFO);

	private HttpMethod(int index, String name, Color color) {
		super(index, name, color);
	}

}
