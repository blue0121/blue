package blue.base.internal.core.security;

import blue.base.core.security.KeyPairMode;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author Jin Zheng
 * @since 2021-06-21
 */
public class GenerateKeyPair extends AbstractKeyPair {
    private final KeyPairMode mode;
    private final int keySize;

    public GenerateKeyPair(KeyPairMode mode, int keySize) {
        this.mode = mode;
        if (mode.isManual()) {
            if (keySize == 0) {
                throw new IllegalArgumentException("keySize is 0");
            }
            this.keySize = keySize;
        } else {
            this.keySize = mode.getSize();
        }
        this.init();
    }

    private void init() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance(mode.getKey());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        generator.initialize(keySize);
        KeyPair keyPair = generator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
}
