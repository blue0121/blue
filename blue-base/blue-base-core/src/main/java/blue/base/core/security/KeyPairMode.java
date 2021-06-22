package blue.base.core.security;

/**
 * @author Jin Zheng
 * @since 2021-06-16
 */
public enum KeyPairMode {

	RSA("RSA", 0),
	RSA512("RSA", 512),
	RSA1024("RSA", 1024),
	RSA2048("RSA", 2048),
	RSA3084("RSA", 3084),
	RSA4096("RSA", 4096),

	DSA("DSA", 0),
	DSA512("DSA", 512),
	DSA768("DSA", 768),
	DSA1024("DSA", 1024),

	EC("EC", 0),
	EC256("EC", 256),
	EC384("EC", 384),
	EC521("EC", 521),
	;

	private String key;
	private int size;

	KeyPairMode(String key, int size) {
		this.key = key;
		this.size = size;
	}

	public boolean isManual() {
		return size == 0;
	}

	public String getKey() {
		return key;
	}

	public int getSize() {
		return size;
	}
}
