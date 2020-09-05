package blue.core.dict;

/**
 * @author Jin Zheng
 * @since 2020-04-11
 */
public abstract class Dictionary
{
	private int index;
	private String name;
	private Color color;

	protected Dictionary(int index, String name, Color color)
	{
		this.index = index;
		this.name = name;
		this.color = color;
	}

	@Override
	public String toString()
	{
		String type = this.getClass().getSimpleName();
		return String.format("%s{%s}", type, name);
	}

	public int getIndex()
	{
		return index;
	}

	public String getName()
	{
		return name;
	}

	public Color getColor()
	{
		return color;
	}
}
