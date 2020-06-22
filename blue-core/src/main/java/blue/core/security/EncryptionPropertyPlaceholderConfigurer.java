package blue.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.util.StringValueResolver;

/**
 * 属性文件解密
 *
 * @author zhengjin
 * @since 1.0 2018年12月14日
 */
public class EncryptionPropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer
{
	private static Logger logger = LoggerFactory.getLogger(EncryptionPropertyPlaceholderConfigurer.class);
	public static final String ENC = "enc:";
	public static final String KEY = "password";

	public EncryptionPropertyPlaceholderConfigurer()
	{
	}

	@Override
	@Deprecated
	protected String convertProperty(String propertyName, String propertyValue)
	{
		String value = propertyValue;
		if (value.startsWith(ENC))
		{
			logger.info("key[{}] - value[{}] 需要解密", propertyName, value);
			value = AESUtil.decrypt(KEY, value.substring(ENC.length()));
		}
		return value;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory factory, final ConfigurablePropertyResolver propertyResolver) throws BeansException
	{

		propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
		propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
		propertyResolver.setValueSeparator(this.valueSeparator);

		StringValueResolver valueResolver = strVal ->
		{
			String resolved = (this.ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(strVal) : propertyResolver.resolveRequiredPlaceholders(strVal));
			if (this.trimValues)
			{
				resolved = resolved.trim();
			}
			if (resolved.startsWith(ENC))
			{
				logger.info("holder[{}] - value[{}] need to decrypt", strVal, resolved);
				resolved = AESUtil.decrypt(KEY, resolved.substring(ENC.length()));
			}
			return (resolved.equals(this.nullValue) ? null : resolved);
		};

		doProcessProperties(factory, valueResolver);
	}

}
