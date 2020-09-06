package blue.internal.core.dict;

import blue.core.dict.DictValue;

/**
 * @author Jin Zheng
 * @since 2020-09-06
 */
public class DefaultDictValue implements DictValue
{
	private int value;
	private String field;
	private String label;

	public DefaultDictValue(int value, String field, String label)
	{
		this.value = value;
		this.field = field;
		this.label = label;
	}

	@Override
	public Integer getValue()
	{
		return value;
	}

	@Override
	public String getField()
	{
		return field;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public int compareTo(DictValue o)
	{
		return value - o.getValue();
	}
}
