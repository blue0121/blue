package test.validation.core.model;

import blue.validation.core.annotation.Vin;
import blue.validation.core.group.SaveGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
public class VinNo {
	@Vin(groups = {SaveGroup.class})
	private String vin;

	public VinNo() {
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

}
