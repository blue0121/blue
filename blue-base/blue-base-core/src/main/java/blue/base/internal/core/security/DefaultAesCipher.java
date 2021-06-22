package blue.base.internal.core.security;

import blue.base.core.security.AesCipher;
import blue.base.core.security.CipherMode;
import blue.base.core.security.Digest;
import blue.base.core.security.DigestType;
import blue.base.core.security.SecurityFactory;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-07
 */
public class DefaultAesCipher implements AesCipher {
	private static Logger logger = LoggerFactory.getLogger(DefaultAesCipher.class);
	private static final String PKCS5_PADDING = "PKCS5Padding";
	private static final String NO_PADDING = "NoPadding";
	private static final String KEY_ALGORITHM = "AES";
	private static final int KEY_LENGTH = 128;
	private static final int BUF_SIZE = 1024;
	private static final int OFFSET = 0;

	private final CipherMode cipherMode;
	private final String algorithm;
	private final Digest digest;

	public DefaultAesCipher(CipherMode cipherMode) {
		this.cipherMode = cipherMode;
		this.digest = SecurityFactory.createDigest(DigestType.MD5);

		if (cipherMode == CipherMode.CTR) {
			this.algorithm = String.format("AES/%s/%s", cipherMode, NO_PADDING);
		} else {
			this.algorithm = String.format("AES/%s/%s", cipherMode, PKCS5_PADDING);
		}
	}

	@Override
	public byte[] generateKey() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
			generator.init(KEY_LENGTH);
			SecretKey key = generator.generateKey();
			return key.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Cipher initCipher(byte[] key, int mode) {
		AssertUtil.notEmpty(key, "Key");
		if (key.length != KEY_LENGTH / 8) {
			key = digest.digest(key);
		}
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			if (cipherMode == CipherMode.ECB) {
				cipher.init(mode, secretKey);
			} else {
				IvParameterSpec ivSpec = new IvParameterSpec(key);
				cipher.init(mode, secretKey, ivSpec);
			}
			return cipher;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void handleStream(byte[] key, InputStream in, OutputStream out, int mode) {
		AssertUtil.notNull(in, "InputStream");
		AssertUtil.notNull(out, "OutputStream");
		Cipher cipher = this.initCipher(key, mode);
		byte[] inBuf = new byte[BUF_SIZE];
		byte[] outBuf = new byte[BUF_SIZE];
		int read = -1;
		int write = -1;
		try (in; out) {
			while ((read = in.read(inBuf)) != -1) {
				write = cipher.update(inBuf, OFFSET, read, outBuf);
				out.write(outBuf, OFFSET, write);
				out.flush();
			}
			byte[] outFinal = cipher.doFinal();
			out.write(outFinal);
			out.flush();
		} catch (Exception e) {
			logger.error("Error, ", e);
		}
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] data) {
		AssertUtil.notEmpty(data, "Data");
		Cipher cipher = this.initCipher(key, Cipher.ENCRYPT_MODE);
		byte[] raw = null;
		try {
			raw = cipher.doFinal(data);
		} catch (Exception e) {
			logger.error("Error, ", e);
		}
		return raw;
	}

	@Override
	public byte[] encrypt(String password, byte[] data) {
		AssertUtil.notEmpty(password, "Password");
		return this.encrypt(password.getBytes(StandardCharsets.UTF_8), data);
	}

	@Override
	public String encryptString(String password, String data) {
		AssertUtil.notEmpty(password, "Password");
		AssertUtil.notEmpty(data, "Data");
		byte[] raw = this.encrypt(password.getBytes(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(raw);
	}

	@Override
	public void encrypt(byte[] key, InputStream in, OutputStream out) {
		this.handleStream(key, in, out, Cipher.ENCRYPT_MODE);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] raw) {
		AssertUtil.notEmpty(raw, "Data");
		Cipher cipher = this.initCipher(key, Cipher.DECRYPT_MODE);
		byte[] data = null;
		try {
			data = cipher.doFinal(raw);
		} catch (Exception e) {
			logger.error("Error, ", e);
		}
		return data;
	}

	@Override
	public byte[] decrypt(String password, byte[] raw) {
		AssertUtil.notEmpty(password, "Password");
		return this.decrypt(password.getBytes(StandardCharsets.UTF_8), raw);
	}

	@Override
	public String decryptString(String password, String rawBase64) {
		AssertUtil.notEmpty(password, "Password");
		AssertUtil.notEmpty(rawBase64, "Raw");
		byte[] raw = Base64.getDecoder().decode(rawBase64);
		byte[] data = this.decrypt(password.getBytes(StandardCharsets.UTF_8), raw);
		return new String(data, StandardCharsets.UTF_8);
	}

	@Override
	public void decrypt(byte[] key, InputStream in, OutputStream out) {
		this.handleStream(key, in, out, Cipher.DECRYPT_MODE);
	}

	@Override
	public CipherMode getCipherMode() {
		return cipherMode;
	}

	@Override
	public String getAlgorithm() {
		return algorithm;
	}


}
