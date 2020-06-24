package blue.internal.jdbc.metadata;

public class TableId extends TableColumn
{
	protected boolean inc = false;

	public TableId()
	{
		this.nullable = false;
	}

	public boolean isInc()
	{
		return inc;
	}

	public void setInc(boolean inc)
	{
		this.inc = inc;
	}

}
