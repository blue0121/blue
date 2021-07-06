package blue.base.internal.starter.property;

/**
 * @author Jin Zheng
 * @since 1.0 2020-07-27
 */
public class HttpHeaderProperties {
	private String name;
	private String value;

	public HttpHeaderProperties() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
