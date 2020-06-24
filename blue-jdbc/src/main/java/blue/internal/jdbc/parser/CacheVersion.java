package blue.internal.jdbc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 缓存持久对象版本字段
 * 
 * @author zhengj
 * @since 1.0 2016年9月9日
 */
public class CacheVersion
{
	private String name;
	private String column;
	private String escapeColumn;
	private boolean force;

	private Field field;
	private Method getterMethod;
	private Method setterMethod;

	public CacheVersion()
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

	public String getColumn()
	{
		return column;
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public String getEscapeColumn()
	{
		return escapeColumn;
	}

	public void setEscapeColumn(String escapeColumn)
	{
		this.escapeColumn = escapeColumn;
	}

	public boolean isForce()
	{
		return force;
	}

	public void setForce(boolean force)
	{
		this.force = force;
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public Method getGetterMethod()
	{
		return getterMethod;
	}

	public void setGetterMethod(Method getterMethod)
	{
		this.getterMethod = getterMethod;
	}

	public Method getSetterMethod()
	{
		return setterMethod;
	}

	public void setSetterMethod(Method setterMethod)
	{
		this.setterMethod = setterMethod;
	}

}
