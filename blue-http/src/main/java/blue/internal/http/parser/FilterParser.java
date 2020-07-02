package blue.internal.http.parser;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-08
 */
public class FilterParser
{
	private static volatile FilterParser instance;

	private FilterParser()
	{
	}

	public static FilterParser getInstance()
	{
		if (instance == null)
		{
			synchronized (FilterParser.class)
			{
				if (instance == null)
				{
					instance = new FilterParser();
				}
			}
		}
		return instance;
	}



}
