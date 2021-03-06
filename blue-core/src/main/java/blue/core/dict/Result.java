package blue.core.dict;

/**
 * 处理结果
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class Result extends Dictionary
{
	public static final Result DELETE = new Result(-1, "删除", Color.WARNING);
	public static final Result CREATE = new Result(0, "创建", Color.INFO);
	public static final Result SUCCESS = new Result(1, "成功", Color.SUCCESS);
	public static final Result FAIL = new Result(2, "失败", Color.DANGER);
	
	private Result(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
