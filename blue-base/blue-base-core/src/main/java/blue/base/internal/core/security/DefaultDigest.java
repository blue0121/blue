package blue.base.internal.core.security;

import blue.base.core.security.Digest;
import blue.base.core.security.DigestType;
import blue.base.core.util.AssertUtil;
import blue.base.core.util.NumberUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-01
 */
public class DefaultDigest implements Digest {
	private static final int SIZE = 4096;
	protected final DigestType type;

	public DefaultDigest(DigestType type) {
		this.type = type;
	}

	@Override
	public byte[] digest(byte[] src) {
		AssertUtil.notEmpty(src, "Source");
		MessageDigest md = this.getMessageDigest();
		return md.digest(src);
	}
	private MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance(type.getKey());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public byte[] digest(String src) {
		AssertUtil.notEmpty(src, "Source");
		return this.digest(src.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public byte[] digest(InputStream is) {
		AssertUtil.notNull(is, "InputStream");
		MessageDigest md = this.getMessageDigest();
		byte[] buf = new byte[SIZE];
		int read = -1;
		try (is) {
			while ((read = is.read(buf)) != -1) {
				md.update(buf, 0, read);
			}
			return md.digest();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public String digestToHex(byte[] src) {
		byte[] bytes = this.digest(src);
		return NumberUtil.toHexString(bytes);
	}

	@Override
	public String digestToHex(String src) {
		AssertUtil.notEmpty(src, "Source");
		byte[] bytes = this.digest(src.getBytes(StandardCharsets.UTF_8));
		return NumberUtil.toHexString(bytes);
	}

	@Override
	public String digestToHex(InputStream is) {
		byte[] bytes = this.digest(is);
		return NumberUtil.toHexString(bytes);
	}

	@Override
	public String digestToBase64(byte[] src) {
		byte[] bytes = this.digest(src);
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Override
	public String digestToBase64(String src) {
		AssertUtil.notEmpty(src, "Source");
		byte[] bytes = this.digest(src.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Override
	public String digestToBase64(InputStream is) {
		byte[] bytes = this.digest(is);
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Override
	public DigestType getDigestType() {
		return type;
	}
}
