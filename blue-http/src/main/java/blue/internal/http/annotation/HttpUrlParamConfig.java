package blue.internal.http.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-18
 */
public class HttpUrlParamConfig
{
	private String name;
	private Parameter parameter;
	private Class<?> paramClazz;
	private Annotation paramAnnotation;
	private String value;
	private boolean required;
	private boolean validated;
	private Class<?>[] validatedGroups;

	public HttpUrlParamConfig()
	{
	}

	public boolean hasParamAnnotation()
	{
		return paramAnnotation != null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Parameter getParameter()
	{
		return parameter;
	}

	public void setParameter(Parameter parameter)
	{
		this.parameter = parameter;
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

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean isValidated()
	{
		return validated;
	}

	public void setValidated(boolean validated)
	{
		this.validated = validated;
	}

	public Class<?>[] getValidatedGroups()
	{
		return validatedGroups;
	}

	public void setValidatedGroups(Class<?>[] validatedGroups)
	{
		this.validatedGroups = validatedGroups;
	}
}
