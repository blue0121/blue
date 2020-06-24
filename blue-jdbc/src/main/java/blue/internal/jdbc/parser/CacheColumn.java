package blue.internal.jdbc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 缓存持久对象字段
 * 
 * @author zhengj
 * @since 2016年7月9日 1.0
 */
public class CacheColumn
{
	private String name;
	private String column;
	private String escapeColumn;
	private boolean mustInsert;
	private boolean mustUpdate;

	private Field field;
	private Method getterMethod;
	private Method setterMethod;

	public CacheColumn()
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

	public boolean isMustInsert()
	{
		return mustInsert;
	}

	public void setMustInsert(boolean mustInsert)
	{
		this.mustInsert = mustInsert;
	}

	public boolean isMustUpdate()
	{
		return mustUpdate;
	}

	public void setMustUpdate(boolean mustUpdate)
	{
		this.mustUpdate = mustUpdate;
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
