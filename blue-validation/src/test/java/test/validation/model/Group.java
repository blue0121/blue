package test.validation.model;


import blue.validation.annotation.FieldEqual;
import blue.validation.annotation.FieldNotEqual;
import blue.validation.group.SaveModel;
import blue.validation.group.UpdateModel;

@FieldEqual(fields={"name", "name3"}, groups={SaveModel.class})
@FieldNotEqual(fields={"name", "name2"}, groups={UpdateModel.class})
public class Group
{
	private String name;
	private String name2;

	public Group()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName2()
	{
		return name2;
	}

	public void setName2(String name2)
	{
		this.name2 = name2;
	}

}
