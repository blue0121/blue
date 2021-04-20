package blue.base.core.dict;

/**
 * 字符编码
 *
 * @author zhengj
 * @since 1.0 2017年3月8日
 */
public final class Charset extends Dictionary {
	public static final Charset UTF_8 = new Charset(1, "UTF-8", Color.PRIMARY);
	public static final Charset GBK = new Charset(2, "GBK", Color.DANGER);

	private Charset(int index, String name, Color color) {
		super(index, name, color);
	}

}
