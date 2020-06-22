package blue.validation.annotation;

/**
 * 密码类型
 *
 * @author zhengjin
 * @since 1.0 2017年12月06日
 */
public enum PasswordType
{
	/**
	 * 明文
	 */
	PLAIN,

	/**
	 * MD5散列
	 */
	MD5,

	/**
	 * SHA-1散列
	 */
	SHA1,
}
