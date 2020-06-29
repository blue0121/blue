package blue.http.annotation;

/**
 * @author Jin Zheng
 * @since 2020-06-16
 */
public enum Charset
{
	UTF_8("UTF-8"),

	GBK("GBK");

	private String name;

	Charset(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
