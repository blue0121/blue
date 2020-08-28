package blue.internal.http.annotation;

import blue.http.annotation.HttpMethod;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2020-08-28
 */
public class HttpUrlKey
{
	private String url;
	private HttpMethod httpMethod;

	public HttpUrlKey()
	{
	}

	public HttpUrlKey(String url, HttpMethod httpMethod)
	{
		this.url = url;
		this.httpMethod = httpMethod;
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
		HttpUrlKey that = (HttpUrlKey) o;
		return url.equals(that.url) &&
				httpMethod == that.httpMethod;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(url, httpMethod);
	}

	@Override
	public String toString()
	{
		return String.format("{url: %s, method: %s}", url, httpMethod.name());
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}
}
