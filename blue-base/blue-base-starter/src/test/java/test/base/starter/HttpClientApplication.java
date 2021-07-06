package test.base.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-15
 */
@SpringBootApplication
public class HttpClientApplication {
	public HttpClientApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(HttpClientApplication.class, args);
	}

}
