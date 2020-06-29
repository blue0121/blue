package blue.internal.http.message;

import blue.http.message.View;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建页面渲染对象
 *
 * @author zhengjin
 * @since 1.0 2018年03月16日
 */
public class ViewBuilder
{
	private String location;
	private String view;
	private Map<String, Object> model;

	public ViewBuilder()
	{
		this.model = new HashMap<>();
	}

	public View build()
	{
		DefaultView view = new DefaultView();
		view.setLocation(location);
		view.setView(this.view);
		view.setModel(model);
		return view;
	}

	public ViewBuilder setLocation(String location)
	{
		this.location = location;
		return this;
	}

	public ViewBuilder setView(String view)
	{
		this.view = view;
		return this;
	}

	public ViewBuilder put(String key, Object value)
	{
		model.put(key, value);
		return this;
	}

	public ViewBuilder remove(String key, Object value)
	{
		model.remove(key);
		return this;
	}


}
