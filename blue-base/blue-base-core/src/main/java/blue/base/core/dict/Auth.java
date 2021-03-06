package blue.base.core.dict;

/**
 * 认证状态
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public final class Auth extends Dictionary {
	public static final Auth UNDONE = new Auth(-1, "未完成", Color.PRIMARY);
	public static final Auth PENDING = new Auth(0, "待审核", Color.INFO);
	public static final Auth APPROVE = new Auth(1, "审核通过", Color.SUCCESS);
	public static final Auth UNAPPROVE = new Auth(2, "审核未通过", Color.DANGER);

	private Auth(int index, String name, Color color) {
		super(index, name, color);
	}
}
