package blue.base.internal.starter.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-15
 */
@ConfigurationProperties("blue.http")
public class HttpProperties {
	private List<HttpConfigProperties> configs;

	public HttpProperties() {
	}

	public List<HttpConfigProperties> getConfigs() {
		return configs;
	}

	public void setConfigs(List<HttpConfigProperties> configs) {
		this.configs = configs;
	}
}
