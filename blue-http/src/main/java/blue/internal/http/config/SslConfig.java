package blue.internal.http.config;

import blue.core.util.FileUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-17
 */
public class SslConfig implements InitializingBean
{
	private static Logger logger = LoggerFactory.getLogger(SslConfig.class);

	private SslContext sslContext;
	private boolean enable = false;
	private String jksPath;
	private String password;
	private String alias;

	public SslConfig()
	{
	}

	public SslContext getSslContext()
	{
		return sslContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (!enable)
			return;

		try
		{
			if (jksPath == null || jksPath.isEmpty())
			{
				SelfSignedCertificate certificate = new SelfSignedCertificate();
				sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
				logger.info("use Netty self signed certificate");
			}
			else
			{
				char[] pwd = password == null ? null : password.toCharArray();
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				try (InputStream is = FileUtil.toInputStream(jksPath))
				{
					ks.load(is, pwd);
				}
				PrivateKey privateKey = (PrivateKey)ks.getKey(alias, pwd);
				X509Certificate x509 = (X509Certificate)ks.getCertificate(alias);
				sslContext = SslContextBuilder.forServer(privateKey, x509).build();
				logger.info("use certificate: {}", jksPath);
			}
		}
		catch (Exception e)
		{
			logger.error("create SslContext error, ", e);
		}
	}

	public boolean isEnable()
	{
		return enable;
	}

	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}

	public String getJksPath()
	{
		return jksPath;
	}

	public void setJksPath(String jksPath)
	{
		this.jksPath = jksPath;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}
}
