package blue.core.reflect;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-03
 */
public interface ColumnNameOperation extends NameOperation
{
	/**
	 * convert to table/column name
	 * @return
	 */
	default String getColumnName()
	{
		String name = this.getName();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (Character.isUpperCase(c))
			{
				if (i > 0)
					sb.append('_');

				sb.append(Character.toLowerCase(c));
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
