package blue.internal.http.parser;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class WebSocketUrlConfig
{
	private String url;
	private int version;

	public WebSocketUrlConfig()
	{
	}

	public WebSocketUrlConfig(String url, int version)
	{
		this.url = url;
		this.version = version;
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WebSocketUrlConfig that = (WebSocketUrlConfig) o;
		return version == that.version && url.equals(that.url);
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
}
