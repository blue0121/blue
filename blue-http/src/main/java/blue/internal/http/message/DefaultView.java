package blue.internal.http.message;

import blue.http.message.View;

import java.util.Map;

/**
 * @author zhengjin
 * @since 1.0 2018年03月16日
 */
public class DefaultView implements View
{
	private String location;
	private String view;
	private Map<String, Object> model;

	public DefaultView()
	{
	}

	@Override
	public String location()
	{
		return location;
	}

	@Override
	public String view()
	{
		return view;
	}

	@Override
	public Map<String, Object> model()
	{
		return model;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public void setView(String view)
	{
		this.view = view;
	}

	public void setModel(Map<String, Object> model)
	{
		this.model = model;
	}
}
