package blue.core.dict;

/**
 * 使用状态
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class UsedState extends Dictionary
{
	public static final UsedState UNUSED = new UsedState(0, "未使用", Color.DANGER);
	public static final UsedState USED = new UsedState(1, "已使用", Color.PRIMARY);
	
	private UsedState(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
