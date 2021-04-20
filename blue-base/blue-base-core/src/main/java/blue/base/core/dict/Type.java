package blue.base.core.dict;

/**
 * 是否类型
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class Type extends Dictionary {
	public static final Type NO = new Type(0, "否", Color.DANGER);
	public static final Type YES = new Type(1, "是", Color.PRIMARY);

	private Type(int index, String name, Color color) {
		super(index, name, color);
	}

}
