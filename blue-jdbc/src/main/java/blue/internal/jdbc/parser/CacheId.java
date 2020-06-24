package blue.internal.jdbc.parser;

import blue.jdbc.annotation.GeneratorType;
import blue.jdbc.annotation.IdType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 缓存持久对象主键
 * 
 * @author zhengj
 * @since 2016年7月9日 1.0
 */
public class CacheId
{
	private IdType idType;
	private GeneratorType generatorType;
	private String name;
	private String column;
	private String escapeColumn;

	private Field field;
	private Method getterMethod;
	private Method setterMethod;

	public CacheId()
	{
	}

	public IdType getIdType()
	{
		return idType;
	}

	public void setIdType(IdType idType)
	{
		this.idType = idType;
	}

	public GeneratorType getGeneratorType()
	{
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType)
	{
		this.generatorType = generatorType;
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
