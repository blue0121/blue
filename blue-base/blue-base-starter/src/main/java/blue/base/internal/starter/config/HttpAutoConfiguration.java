package blue.base.internal.starter.config;

import blue.base.internal.starter.property.HttpProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-15
 */
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
@ConditionalOnProperty(prefix = "blue.http", name = "enabled", havingValue = "true")
public class HttpAutoConfiguration {

	public HttpAutoConfiguration() {
	}

	@Bean
	public static HttpBeanPostProcessor httpBeanPostProcessor() {
		return new HttpBeanPostProcessor();
	}

}
