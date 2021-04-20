package blue.base.core.dict;

/**
 * 阅读状态
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class Read extends Dictionary {
	public static final Read UNREAD = new Read(0, "未读", Color.DANGER);
	public static final Read READ = new Read(1, "已读", Color.PRIMARY);

	private Read(int index, String name, Color color) {
		super(index, name, color);
	}

}
