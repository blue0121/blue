package blue.base.internal.core.security;

import blue.base.core.security.KeyPairMode;
import blue.base.core.util.AssertUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * @author Jin Zheng
 * @since 2021-06-09
 */
public class LoadOpenSslKeyPair extends AbstractKeyPair {
	private static final String MINUS = "-";
	private static final String CERT = "X.509";

	private final String algorithm;

	public LoadOpenSslKeyPair(KeyPairMode mode, InputStream...inputs) {
        AssertUtil.notEmpty(inputs, "InputStream");
        this.algorithm = mode.getKey();
		for (var in : inputs) {
			this.read(in);
		}
		AssertUtil.notNull(publicKey, "PublicKey");
		AssertUtil.notNull(privateKey, "PrivateKey");
	}

	private void read(InputStream in) {
		StringBuilder str = new StringBuilder();
		int i = 0;
		String type = null;
		try (Scanner scanner = new Scanner(in)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line == null || line.isEmpty())
					continue;
				if (i == 0 && line.startsWith(MINUS)) {
					type = line;
					continue;
				}
				if (line.startsWith(MINUS))
					continue;

				str.append(line);
				i++;
			}
		}
		if (type == null || type.isEmpty())
			return;

		byte[] bytes = Base64.getDecoder().decode(str.toString());
		if (type.contains("BEGIN CERTIFICATE")) {
			this.readCertificate(bytes);
		} else if (type.contains("BEGIN PUBLIC KEY")) {
			this.readPublicKey(bytes);
		} else if (type.contains("BEGIN PRIVATE KEY")) {
			this.readPrivateKey(bytes);
		}

	}

	private void readCertificate(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		try {
			CertificateFactory factory = CertificateFactory.getInstance(CERT);
			this.certificate = (X509Certificate) factory.generateCertificate(is);
			this.publicKey = certificate.getPublicKey();
		} catch (CertificateException e) {
			throw new IllegalArgumentException(e);
		}
	}

    private void readPublicKey(byte[] bytes) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            this.publicKey = factory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void readPrivateKey(byte[] bytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            this.privateKey = factory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
