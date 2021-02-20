package blue.internal.core.reflect;

import blue.core.util.AssertUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2021-02-20
 */
public class ParamClassKey
{
	private final Class<?>[] paramClasses;

	public ParamClassKey(Class<?>[] paramClasses)
	{
		AssertUtil.notNull(paramClasses, "Parameter class array");
		this.paramClasses = paramClasses;
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	public ParamClassKey(Collection<?> cols)
	{
		AssertUtil.notNull(cols, "Parameter class array");
		this.paramClasses = cols.toArray(new Class[0]);
	}

	public Class<?>[] getParamClasses()
	{
		return paramClasses;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		ParamClassKey that = (ParamClassKey) o;
		return Arrays.equals(paramClasses, that.paramClasses);
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(paramClasses);
	}
}
