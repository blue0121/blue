package blue.internal.http.message;

import blue.core.id.IdGenerator;

/**
 * @author Jin Zheng
 * @since 2020-01-29
 */
public class WebSocketHeader
{
	private long messageId; // 消息标识
	private String token; // 令牌
	private long timestamp = System.currentTimeMillis(); // 当前时间戳
	private String url; // URL
	private int version; // 版本
	private int type; // 1:发送，2:接收

	public WebSocketHeader()
	{
	}

	public static WebSocketHeader from(WebSocketHeader request)
	{
		WebSocketHeader response = new WebSocketHeader();
		if (request != null)
		{
			response.setMessageId(request.messageId);
			response.setToken(request.getToken());
			response.setUrl(request.getUrl());
			response.setVersion(request.getVersion());
		}
		else
		{
			response.setMessageId(IdGenerator.id());
		}
		response.setType(2);
		return response;
	}

	public long getMessageId()
	{
		return messageId;
	}

	public void setMessageId(long messageId)
	{
		this.messageId = messageId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
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

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
