package blue.internal.http.annotation;

import blue.http.annotation.Validated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class RequestParamConfig
{
	private String name;
	private Parameter param;
	private Class<?> paramClazz;
	private Annotation paramAnnotation;
	private Validated validAnnotation;

	public RequestParamConfig()
	{
	}

	public boolean hasParamAnnotation()
	{
		return paramAnnotation != null;
	}

	public boolean hasValidAnnotation()
	{
		return validAnnotation != null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Parameter getParam()
	{
		return param;
	}

	public void setParam(Parameter param)
	{
		this.param = param;
	}

	public Class<?> getParamClazz()
	{
		return paramClazz;
	}

	public void setParamClazz(Class<?> paramClazz)
	{
		this.paramClazz = paramClazz;
	}

	public Annotation getParamAnnotation()
	{
		return paramAnnotation;
	}

	public void setParamAnnotation(Annotation paramAnnotation)
	{
		this.paramAnnotation = paramAnnotation;
	}

	public Validated getValidAnnotation()
	{
		return validAnnotation;
	}

	public void setValidAnnotation(Validated validAnnotation)
	{
		this.validAnnotation = validAnnotation;
	}
}
