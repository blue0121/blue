package test.validation.annotation;

import blue.validation.ValidationUtil;
import blue.validation.group.SaveModel;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.model.VinNo;

public class VinTest {

	public VinTest() {
	}

	@Test
	public void test1() throws Exception
	{
		VinNo vinNo = new VinNo();
		vinNo.setVin("LFV3A28K8C303775");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(vinNo, SaveModel.class));
	}
	
	@Test
	public void test2() throws Exception
	{
		VinNo vinNo = new VinNo();
		vinNo.setVin("LfV3A28K8C3037756");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(vinNo, SaveModel.class));
	}
	
	@Test
	public void test3() throws Exception
	{
		VinNo vinNo = new VinNo();
		vinNo.setVin("LFV3A28K8C3037753");
		ValidationUtil.valid(vinNo, SaveModel.class);
	}
	
	@Test
	public void test4() throws Exception
	{
		VinNo vinNo = new VinNo();
		vinNo.setVin("LFV3A28K8C3037756");
		ValidationUtil.valid(vinNo, SaveModel.class);
	}

}
