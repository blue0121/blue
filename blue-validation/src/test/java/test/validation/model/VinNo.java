package test.validation.model;


import blue.validation.annotation.Vin;
import blue.validation.group.SaveModel;

public class VinNo {
	@Vin(groups={SaveModel.class})
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
