package blue.base.internal.core.security;

import blue.base.core.security.Signature;
import blue.base.core.security.SignatureMode;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public class DefaultSignature implements Signature {
	private static Logger logger = LoggerFactory.getLogger(DefaultSignature.class);
	private static final int BUF_SIZE = 4096;

	private final SignatureMode mode;

	public DefaultSignature(SignatureMode mode) {
		this.mode = mode;
	}

	@Override
	public byte[] sign(PrivateKey key, byte[] data) {
		AssertUtil.notNull(key, "PrivateKey");
		AssertUtil.notEmpty(data, "Data");
		try {
			java.security.Signature signature = java.security.Signature.getInstance(mode.getKey());
			signature.initSign(key);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String signString(PrivateKey key, String data) {
		AssertUtil.notEmpty(data, "Data");
		byte[] sign = this.sign(key, data.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(sign);
	}

	@Override
	public byte[] sign(PrivateKey key, InputStream in) {
		AssertUtil.notNull(in, "InputStream");
		byte[] buf = new byte[BUF_SIZE];
		int read = -1;
		try (in) {
			java.security.Signature signature = java.security.Signature.getInstance(mode.getKey());
			signature.initSign(key);
			while ((read = in.read(buf)) != -1) {
				signature.update(buf, 0, read);
			}
			return signature.sign();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public boolean verify(PublicKey key, byte[] data, byte[] sign) {
		AssertUtil.notNull(key, "PublicKey");
		AssertUtil.notEmpty(data, "Data");
		AssertUtil.notEmpty(sign, "SignData");
		try {
			java.security.Signature signature = java.security.Signature.getInstance(mode.getKey());
			signature.initVerify(key);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public boolean verifyString(PublicKey key, String data, String signBase64) {
		AssertUtil.notEmpty(data, "Data");
		AssertUtil.notEmpty(signBase64, "Sign Base64 string");
		byte[] sign = Base64.getDecoder().decode(signBase64);
		return this.verify(key, data.getBytes(StandardCharsets.UTF_8), sign);
	}

	@Override
	public boolean verify(PublicKey key, InputStream in, byte[] sign) {
		AssertUtil.notNull(in, "InputStream");
		byte[] buf = new byte[BUF_SIZE];
		int read = -1;
		try (in) {
			java.security.Signature signature = java.security.Signature.getInstance(mode.getKey());
			signature.initVerify(key);
			while ((read = in.read(buf)) != -1) {
				signature.update(buf, 0, read);
			}
			return signature.verify(sign);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
