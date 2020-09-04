package blue.core.dict;

/**
 * 操作方式
 * 
 * @author zhengj
 * @since 1.0 2017年5月23日
 */
public final class OperateType extends Dictionary
{
	public static final OperateType UNKNOWN = new OperateType(0, "未知", Color.BLACK);
	public static final OperateType ADD = new OperateType(1, "添加", Color.PRIMARY);
	public static final OperateType UPDATE = new OperateType(2, "更新", Color.WARNING);
	public static final OperateType DELETE = new OperateType(3, "删除", Color.DANGER);
	public static final OperateType VIEW = new OperateType(4, "查看", Color.INFO);

	private OperateType(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
