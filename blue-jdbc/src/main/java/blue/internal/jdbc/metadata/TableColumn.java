package blue.internal.jdbc.metadata;

public class TableColumn
{
	protected String column;
	protected int type;
	protected String typeName;
	protected boolean nullable = true;
	protected int size;
	protected String def;
	protected String comment;

	public TableColumn()
	{
	}

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public boolean isNullable()
	{
		return nullable;
	}

	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getDef()
	{
		return def;
	}

	public void setDef(String def)
	{
		this.def = def;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

}
