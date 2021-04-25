package blue.internal.http.message;

import blue.http.message.WebSocketRequest;
import blue.validation.core.ValidationUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2020-02-05
 */
public class DefaultWebSocketRequest implements WebSocketRequest {
	private String token;
	private long timestamp;
	private String url;
	private int version;
	private int userId;
	private Object data;

	private DefaultWebSocketRequest() {
	}

	public static WebSocketRequest from(WebSocketMessage message) {
		if (message == null || message.getHeader() == null) {
			return null;
		}

		DefaultWebSocketRequest request = new DefaultWebSocketRequest();
		WebSocketBody body = message.getBody();
		WebSocketHeader header = message.getHeader();
		request.token = header.getToken();
		request.timestamp = header.getTimestamp();
		request.url = header.getUrl();
		request.version = header.getVersion();
		request.data = body.getData();
		return request;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public <T> T getObject(Class<T> clazz, Class<?>... groups) {
		T target = null;
		if (data != null) {
			if (data instanceof JSONObject) {
				target = ((JSONObject) data).toJavaObject(clazz);
			}
			else {
				target = (T) data;
			}
		}
		if (target != null) {
			ValidationUtil.valid(target, groups);
		}
		return target;
	}

	@Override
	public <T> List<T> getObjectList(Class<T> clazz, Class<?>... groups) {
		List<T> target = null;
		if (data != null) {
			if (data instanceof JSONArray) {
				target = ((JSONArray) data).toJavaList(clazz);
			}
			else {
				target = (List<T>) data;
			}
		}
		if (target != null && !target.isEmpty()) {
			for (T t : target) {
				ValidationUtil.valid(t, groups);
			}
		}
		return target;
	}

	@Override
	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public int getUserId() {
		return userId;
	}
}
