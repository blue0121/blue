package blue.http.message;

import blue.http.annotation.HttpMethod;
import io.netty.channel.ChannelId;

import java.util.Map;

/**
 * HTTP 请求参数
 *
 * @author Jin Zheng
 * @since 1.0 2020-01-03
 */
public interface Request
{
	/**
	 * 获取应用根目录
	 *
	 * @return 应用根目录
	 */
	String getPath();

	/**
	 * 获取 URL 的请求路径，不带参数
	 *
	 * @return URL 的请求路径
	 */
	String getUrl();

	/**
	 * 获取 HTTP 请求方法
	 *
	 * @return HTTP 请求方法
	 */
	HttpMethod getHttpMethod();

	/**
	 * 获取真实的IP地址
	 *
	 * @return IP地址
	 */
	String getIp();

	/**
	 * 获取开始执行的时间戳
	 *
	 * @return 开始执行的时间戳
	 */
	long getStart();

	/**
	 * 获取本次请求内容的总长度
	 *
	 * @return 请求内容的总长度
	 */
	long getContentLength();

	/**
	 * 是否微信浏览器
	 *
	 * @return 是微信浏览器返回true，否则返回false
	 */
	boolean isWechat();

	/**
	 * 获取 ChannelId
	 *
	 * @return ChannelId
	 */
	ChannelId getChannelId();

	/**
	 * 把 URL 的请求参数转化为对象并验证
	 *
	 * @param clazz 对象的类型
	 * @param groups 验证组
	 * @return 对象
	 */
	<T> T getQueryObject(Class<T> clazz, Class<?>... groups);

	/**
	 * 获取 URL 的请求参数，用 & 连接
	 *
	 * @return URL 的请求参数
	 */
	String getQueryString();

	/**
	 * 根据名称获取单个 URL 的请求参数
	 *
	 * @param name 参数名称
	 * @return 单个 URL 请求参数
	 */
	String getQueryString(String name);

	/**
	 * 根据名称获取单个 URL 的请求参数的整型值
	 *
	 * @param name 参数名称
	 * @param defaultVal 默认值
	 * @return 单个 URL 请求参数的整型值
	 */
	int getQueryInt(String name, int defaultVal);

	/**
	 * 根据名称获取单个 URL 的请求参数的浮点值
	 *
	 * @param name 参数名称
	 * @param defaultVal 默认值
	 * @return 单个 URL 请求参数的浮点值
	 */
	double getQueryDouble(String name, double defaultVal);

	/**
	 * 获取全部 URL 的请求参数，键-值对
	 *
	 * @return 全部 URL 的请求参数
	 */
	Map<String, String> getQueryStringMap();

	/**
	 * 根据名称获取单个 URL 的路径参数
	 *
	 * @param name 参数名称
	 * @return 单个 URL 路径参数
	 */
	String getPathVariable(String name);

	/**
	 * 获取全部 URL 的路径参数，键-值对
	 *
	 * @return 全部 URL 的路径参数
	 */
	Map<String, String> getPathVariableMap();

	/**
	 * 获取 POST 长度小于 16k 的字符串请求
	 *
	 * @return POST 字符串请求
	 */
	String getContent();

	/**
	 * 把 POST 长度小于 16k 的字符串转化为 json 对象并验证
	 *
	 * @param clazz 对象的类型
	 * @param groups 验证组
	 * @return 对象
	 */
	<T> T getContentJson(Class<T> clazz, Class<?>... groups);

	/**
	 * 获取全部 POST 长度小于 16k 的字符串请求，转化为键-值对
	 *
	 * @return 转化为键-值对 POST 字符串请求
	 */
	Map<String, String> getPostMap();

	/**
	 * 获取单个 POST 长度小于 16k 的字符串请求
	 *
	 * @param name 参数名称
	 * @return 单个 POST 请求参数
	 */
	String getPost(String name);

	/**
	 * 获取单个 POST 长度小于 16k的请求参数的整型值
	 *
	 * @param name 参数名称
	 * @param defaultVal 默认值
	 * @return 单个 URL 请求参数的整型值
	 */
	int getPostInt(String name, int defaultVal);

	/**
	 * 获取单个 POST 长度小于 16k的请求参数的浮点值
	 *
	 * @param name 参数名称
	 * @param defaultVal 默认值
	 * @return 单个 URL 请求参数的浮点值
	 */
	double getPostDouble(String name, double defaultVal);

	/**
	 * 获取单个 POST 长度小于 16k的请求参数转化为对象并验证
	 *
	 * @param clazz 对象的类型
	 * @param groups 验证组
	 * @return 对象
	 */
	<T> T getPostObject(Class<T> clazz, Class<?>... groups);

	/**
	 * 获取全部上传文件，转化为键-值对
	 *
	 * @return 全部上传文件
	 */
	Map<String, UploadFile> getFileMap();

	/**
	 * 获取单个上传文件
	 *
	 * @param name 参数名称
	 * @return 单个文件
	 */
	UploadFile getFile(String name);

	/**
	 * 获取单个上传文件
	 *
	 * @return 单个文件
	 */
	UploadFile getFile();

	/**
	 * 获取所有 Cookie，转化为键-值对
	 *
	 * @return 所有 Cookie
	 */
	Map<String, String> getCookieMap();

	/**
	 * 根据 Cookie 键获取值
	 *
	 * @param name Cookie 键名
	 * @return Cookie 值
	 */
	String getCookie(String name);

	/**
	 * 根据 Header 名称获取值
	 *
	 * @param name Header 名称
	 * @return Header 值
	 */
	String getHeader(String name);

}
