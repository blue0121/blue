package blue.base.internal.core.security;

import blue.base.core.security.RsaCipher;
import blue.base.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * @author Jin Zheng
 * @since 2021-06-15
 */
public class DefaultRsaCipher implements RsaCipher {
    private static Logger logger = LoggerFactory.getLogger(DefaultRsaCipher.class);
    private static final String ALGORITHM = "RSA";

	public DefaultRsaCipher() {
	}

    private Cipher initCipher(Key key, int mode) {
        AssertUtil.notNull(key, "Key");
        if (!(key instanceof RSAPrivateKey) && !(key instanceof RSAPublicKey)) {
            throw new IllegalArgumentException("Key is not RSAPublicKey/RSAPrivateKey");
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(mode, key);
            return cipher;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] encrypt(PublicKey key, byte[] data) {
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
    public String encryptString(PublicKey key, String data) {
        AssertUtil.notEmpty(data, "Data");
        byte[] raw = this.encrypt(key, data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(raw);
    }

    @Override
    public byte[] decrypt(PrivateKey key, byte[] raw) {
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
    public String decryptString(PrivateKey key, String rawBase64) {
        AssertUtil.notEmpty(rawBase64, "Raw");
        byte[] raw = Base64.getDecoder().decode(rawBase64);
        byte[] data = this.decrypt(key, raw);
        return new String(data, StandardCharsets.UTF_8);
    }
}
