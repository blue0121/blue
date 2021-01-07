package blue.jms;

/**
 * @author Jin Zheng
 * @since 1.0 2019-08-02
 */
public enum JmsType
{
	QUEUE(1),
	TOPIC(2);

	private int type;

	JmsType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public static JmsType valueOf(int type)
	{
		for (JmsType jmsType : JmsType.values())
		{
			if (jmsType.type == type)
				return jmsType;
		}
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("%s{%d}", this.name(), type);
	}

}
