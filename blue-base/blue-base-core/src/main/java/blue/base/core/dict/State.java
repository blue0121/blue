package blue.base.core.dict;

/**
 * 通用状态
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class State extends Dictionary {
	public static final State NORMAL = new State(0, "正常", Color.PRIMARY);
	public static final State DELETE = new State(1, "作废", Color.DANGER);

	private State(int index, String name, Color color) {
		super(index, name, color);
	}
}
