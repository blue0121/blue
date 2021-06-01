package blue.base.core.security;

import java.io.InputStream;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public interface Digest {

	/**
	 * @param src
	 * @return
	 */
	byte[] digest(byte[] src);

	/**
	 * @param src
	 * @return
	 */
	byte[] digest(String src);

	byte[] digest(InputStream is);

	String digestToHex(byte[] src);

	String digestToHex(String src);

	String digestToHex(InputStream is);


	String digestToBase64(byte[] src);

	String digestToBase64(String src);

	String digestToBase64(InputStream is);


	DigestType getDigestType();

}
