package blue.internal.http.annotation;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class WebSocketUrlKey
{
	private String url;
	private int version;

	public WebSocketUrlKey()
	{
	}

	public WebSocketUrlKey(String url, int version)
	{
		this.url = url;
		this.version = version;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		WebSocketUrlKey that = (WebSocketUrlKey) o;
		return version == that.version &&
				url.equals(that.url);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(url, version);
	}

	@Override
	public String toString()
	{
		return String.format("{url: %s, version: %d}", url, version);
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}
}
