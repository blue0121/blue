package blue.internal.http.message;

import blue.core.util.BeanUtil;
import blue.core.util.StringUtil;
import blue.core.util.UrlUtil;
import blue.http.annotation.HttpMethod;
import blue.http.message.Request;
import blue.http.message.UploadFile;
import blue.validation.core.ValidationUtil;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public class DefaultRequest implements Request {
	public static final String PARAM_SPLIT = ",";
	public static final String PARAM_CONCAT = "&";
	public static final String PARAM_EQUAL = "=";
	private static Logger logger = LoggerFactory.getLogger(DefaultRequest.class);

	private ChannelId channelId;
	private HttpMethod httpMethod; // http 请求方法
	private String ip; // 请求IP地址
	private long start; // 开始时间戳

	private String path; // 应用根路径
	private String url; // http 路径
	private Map<String, String> queryMap = new HashMap<>(); // URL参数
	private String queryString; // URL参数
	private Map<String, String> pathMap = new HashMap<>(); // Path 参数

	private long contentLength; // 内容长度
	private HttpHeaders headers; // Http请求头
	private String content; // POST内容
	private Map<String, String> postMap = new HashMap<>(); // POST参数
	private Map<String, UploadFile> fileMap = new HashMap<>(); // 上传文件

	private Map<String, String> cookieMap = new HashMap<>(); // Cookie

	public DefaultRequest(HttpMethod httpMethod, String ip, ChannelId id) {
		this.httpMethod = httpMethod;
		this.ip = ip;
		this.channelId = id;
		this.start = System.currentTimeMillis();
	}

	/**
	 * 解析Uri
	 *
	 * @param uri
	 */
	public void parseUri(String uri, String path) {
		this.path = path;
		QueryStringDecoder decoder = new QueryStringDecoder(uri, true);
		this.url = UrlUtil.getRealPath(path, decoder.path());

		List<String> queryList = new ArrayList<>();
		Map<String, List<String>> paramList = decoder.parameters();
		for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
			String value = StringUtil.join(entry.getValue(), PARAM_SPLIT);
			queryMap.put(entry.getKey(), value);
			if (value == null || value.isEmpty()) {
				queryList.add(entry.getKey() + PARAM_EQUAL);
			}
			else {
				queryList.add(entry.getKey() + PARAM_EQUAL + value);
			}
		}
		queryString = StringUtil.join(queryList, PARAM_CONCAT);
	}

	public void parseHeaders(HttpHeaders headers) {
		if (headers == null) {
			return;
		}

		this.headers = headers;
		String cookie = headers.get(HttpHeaderNames.COOKIE);
		if (cookie == null || cookie.isEmpty()) {
			return;
		}

		Set<Cookie> set = ServerCookieDecoder.STRICT.decode(cookie);
		for (Cookie c : set) {
			cookieMap.put(c.name(), c.value());
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void putPost(String name, String value) {
		postMap.put(name, value);
	}

	public void putFile(String name, UploadFile file) {
		fileMap.put(name, file);
	}

	public void putPath(String name, String value) {
		pathMap.put(name, value);
	}

	public void putPath(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return;
		}

		pathMap.putAll(map);
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public long getStart() {
		return start;
	}

	@Override
	public long getContentLength() {
		return contentLength;
	}

	@Override
	public boolean isWechat() {
		String agent = headers.get(HttpHeaderNames.USER_AGENT);
		logger.debug("User-Agent: {}", agent);
		return agent.indexOf("MicroMessenger") != -1;
	}

	@Override
	public ChannelId getChannelId() {
		return channelId;
	}

	@Override
	public <T> T getQueryObject(Class<T> clazz, Class<?>... groups) {
		return this.getObjectFromMap(queryMap, clazz, groups);
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public String getQueryString(String name) {
		return queryMap.get(name);
	}

	@Override
	public int getQueryInt(String name, int defaultVal) {
		return this.getIntFromMap(postMap, name, defaultVal);
	}

	@Override
	public double getQueryDouble(String name, double defaultVal) {
		return this.getDoubleFromMap(postMap, name, defaultVal);
	}

	@Override
	public Map<String, String> getQueryStringMap() {
		return Map.copyOf(queryMap);
	}

	@Override
	public String getPathVariable(String name) {
		return pathMap.get(name);
	}

	@Override
	public Map<String, String> getPathVariableMap() {
		return Map.copyOf(pathMap);
	}

	@Override
	public Map<String, String> getCookieMap() {
		return Map.copyOf(cookieMap);
	}

	@Override
	public String getCookie(String name) {
		return cookieMap.get(name);
	}

	@Override
	public String getHeader(String name) {
		return headers.get(name);
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public <T> T getContentJson(Class<T> clazz, Class<?>... groups) {
		return ValidationUtil.valid(clazz, content, groups);
	}

	@Override
	public Map<String, String> getPostMap() {
		return Map.copyOf(postMap);
	}

	@Override
	public String getPost(String name) {
		return postMap.get(name);
	}

	@Override
	public int getPostInt(String name, int defaultVal) {
		return this.getIntFromMap(postMap, name, defaultVal);
	}

	@Override
	public double getPostDouble(String name, double defaultVal) {
		return this.getDoubleFromMap(postMap, name, defaultVal);
	}

	@Override
	public <T> T getPostObject(Class<T> clazz, Class<?>... groups) {
		return this.getObjectFromMap(postMap, clazz, groups);
	}

	@Override
	public Map<String, UploadFile> getFileMap() {
		return Map.copyOf(fileMap);
	}

	@Override
	public UploadFile getFile(String name) {
		return fileMap.get(name);
	}

	@Override
	public UploadFile getFile() {
		Iterator<UploadFile> iterator = fileMap.values().iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}

	private String getStringFromMap(Map<String, String> map, String key) {
		String str = queryMap.get(key);
		if (str == null || str.isEmpty()) {
			return null;
		}

		str = str.trim();
		if (str.isEmpty()) {
			return null;
		}

		return str;
	}

	private int getIntFromMap(Map<String, String> map, String key, int defaultVal) {
		String str = this.getStringFromMap(map, key);
		if (str == null) {
			return defaultVal;
		}

		try {
			defaultVal = Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			logger.warn("无法转换成整数：{}，用默认值：{}", str, defaultVal);
		}
		return defaultVal;
	}

	private double getDoubleFromMap(Map<String, String> map, String key, double defaultVal) {
		String str = this.getStringFromMap(map, key);
		if (str == null) {
			return defaultVal;
		}

		try {
			defaultVal = Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			logger.warn("无法转换成浮点数：{}，用默认值：{}", str, defaultVal);
		}
		return defaultVal;
	}

	private <T> T getObjectFromMap(Map<String, String> map, Class<T> clazz, Class<?>... groups) {
		T obj = BeanUtil.createBean(clazz, map);
		if (obj != null) {
			ValidationUtil.valid(obj, groups);
		}
		return obj;
	}

}
