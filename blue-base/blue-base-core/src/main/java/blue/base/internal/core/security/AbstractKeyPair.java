package blue.base.internal.core.security;

import blue.base.core.security.KeyPair;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author Jin Zheng
 * @since 2021-06-09
 */
public abstract class AbstractKeyPair implements KeyPair {

	protected X509Certificate certificate;
	protected PublicKey publicKey;
	protected PrivateKey privateKey;

	public AbstractKeyPair() {
	}


	public Certificate getCertificate() {
		return certificate;
	}

	@Override
	public PublicKey getPublic() {
		return publicKey;
	}

	@Override
	public PrivateKey getPrivate() {
		return privateKey;
	}
}
