package blue.core.dict;

/**
 * 运行状态
 * 
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class RunState extends Dictionary
{
	public static final RunState NOT_RUNNIG = new RunState(0, "未运行", Color.RED);
	public static final RunState RUNNING = new RunState(1, "运行中", Color.BLUE);
	public static final RunState RUN = new RunState(2, "已运行", Color.BLACK);
	
	private RunState(int index, String name, Color color)
	{
		super(index, name, color);
	}

}
