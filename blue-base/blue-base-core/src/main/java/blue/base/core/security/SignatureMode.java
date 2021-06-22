package blue.base.core.security;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public enum SignatureMode {

	MD5_RSA("MD5WithRSA"),

	SHA1_RSA("SHA1WithRSA"),
	SHA224_RSA("SHA224WithRSA"),
	SHA256_RSA("SHA256WithRSA"),
	SHA384_RSA("SHA384WithRSA"),
	SHA512_RSA("SHA512WithRSA"),

	SHA1_DSA("SHA1WithDSA"),
	SHA224_DSA("SHA224WithDSA"),
	SHA256_DSA("SHA256WithDSA"),
	SHA384_DSA("SHA384WithDSA"),
	SHA512_DSA("SHA512WithDSA"),

	SHA1_ECDSA("SHA1WithECDSA"),
	SHA224_ECDSA("SHA224WithECDSA"),
	SHA256_ECDSA("SHA256WithECDSA"),
	SHA384_ECDSA("SHA384WithECDSA"),
	SHA512_ECDSA("SHA512WithECDSA");


	private String key;
	SignatureMode(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
