package blue.core.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 颜色
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public enum Color
{
	PRIMARY("主要"),
	INFO("信息"),
	SUCCESS("成功"),
	WARNING("警告"),
	DANGER("危险"),

	BLACK("黑色"),
	SILVER("银色"),
	GRAY("灰色"),
	MAROON("褐红色"),
	RED("红色"),
	PURPLE("紫色"),
	FUCHSIA("桃红色"),
	GREEN("绿色"),
	LIME("青色"),
	OLIVE("橄榄色"),
	YELLOW("黄色"),
	NAVY("深蓝色"),
	BLUE("蓝色"),
	TEAL("蓝绿色"),
	AQUA("浅绿色");

	private String name;
	
	Color(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	/**
	 * 返回所有颜色配置
	 * 
	 * @return Map，key为颜色英文，value为颜色中文
	 */
	public static Map<String, String> getStringMap()
	{
		Map<String, String> map = new HashMap<>();
		for (Color color : Color.values())
		{
			map.put(color.name.toLowerCase(), color.getName());
		}
		return map;
	}
	
}
