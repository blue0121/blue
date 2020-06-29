package blue.http.annotation;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public enum ContentType
{
	AUTO("auto"),

	JSON("application/json; charset=utf-8"),

	HTML("text/html; charset=utf-8");

	private String name;

	ContentType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
