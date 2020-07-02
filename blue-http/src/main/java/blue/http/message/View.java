package blue.http.message;

import java.util.Map;

/**
 * 页面渲染
 *
 * @author zhengjin
 * @since 1.0 2017年11月20日
 */
public interface View
{

	/**
	 * 获取客户端跳转URL
	 * @return
	 */
	String location();

	/**
	 * 获取服务端渲染页面
	 * @return
	 */
	String view();

	/**
	 * 获取服务端页面渲染数据
	 * @return
	 */
	Map<String, Object> model();

	static ViewBuilder createBuilder()
	{
		ViewBuilder builder = new ViewBuilder();
		return builder;
	}

}
