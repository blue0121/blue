package test.validation.core.model;

import blue.validation.core.annotation.FieldEqual;
import blue.validation.core.annotation.FieldNotEqual;
import blue.validation.core.group.SaveGroup;
import blue.validation.core.group.UpdateGroup;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-23
 */
@FieldEqual(fields = {"name", "name3"}, groups = {SaveGroup.class})
@FieldNotEqual(fields = {"name", "name2"}, groups = {UpdateGroup.class})
public class Group {
	private String name;
	private String name2;

	public Group() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

}
