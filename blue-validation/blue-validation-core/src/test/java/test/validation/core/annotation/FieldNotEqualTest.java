package test.validation.core.annotation;

import blue.validation.core.ValidationUtil;
import blue.validation.core.group.UpdateGroup;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.validation.core.model.Group;

public class FieldNotEqualTest {
	public FieldNotEqualTest() {
	}

	@Test
	public void test1() throws Exception {
		Group group = new Group();
		ValidationUtil.valid(group, UpdateGroup.class);
	}

	@Test
	public void test2() throws Exception {
		Group group = new Group();
		group.setName("blue");
		ValidationUtil.valid(group, UpdateGroup.class);
	}

	@Test
	public void test3() throws Exception {
		Group group = new Group();
		group.setName2("blue2");
		ValidationUtil.valid(group, UpdateGroup.class);
	}

	@Test
	public void test4() throws Exception {
		Group group = new Group();
		group.setName("blue");
		group.setName2("blue2");
		ValidationUtil.valid(group, UpdateGroup.class);
	}

	@Test
	public void test5() throws Exception {
		Group group = new Group();
		group.setName("blue");
		group.setName2("blue");
		Assertions.assertThrows(ValidationException.class, () -> ValidationUtil.valid(group, UpdateGroup.class));
	}

}
