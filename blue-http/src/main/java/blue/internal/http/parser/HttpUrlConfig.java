package blue.internal.http.parser;

import blue.http.annotation.HttpMethod;

/**
 * @author Jin Zheng
 * @since 2019-12-29
 */
public class HttpUrlConfig
{
	private String url;
	private HttpMethod method;

	public HttpUrlConfig()
	{
	}

	public HttpUrlConfig(String url, HttpMethod method)
	{
		this.url = url;
		this.method = method;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public HttpMethod getMethod()
	{
		return method;
	}

	public void setMethod(HttpMethod method)
	{
		this.method = method;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpUrlConfig other = (HttpUrlConfig)obj;
		if (method != other.method)
			return false;
		if (url == null)
		{
			if (other.url != null)
				return false;
		}
		else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("{url: %s, method: %s}", url, method.name());
	}

}
