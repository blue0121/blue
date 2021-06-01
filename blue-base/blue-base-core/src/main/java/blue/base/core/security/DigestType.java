package blue.base.core.security;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public enum DigestType {

	MD5("MD5"),

	SHA1("SHA-1"),

	SHA256("SHA-256"),

	SHA384("SHA-384"),

	SHA512("SHA-512");

	private String key;
	DigestType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
