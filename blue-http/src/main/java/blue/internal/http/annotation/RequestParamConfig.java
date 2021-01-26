package blue.internal.http.annotation;

/**
 * @author Jin Zheng
 * @since 1.0 2021-01-22
 */
public class RequestParamConfig
{
	private String name;
	private Class<?> paramClazz;
	private Class<?> paramAnnotationClazz;
	private String paramAnnotationValue;
	private boolean paramAnnotationRequired;
	private boolean validated = false;
	private Class<?>[] validatedGroups;

	public RequestParamConfig()
	{
	}

	public boolean hasParamAnnotation()
	{
		return paramAnnotationClazz != null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Class<?> getParamClazz()
	{
		return paramClazz;
	}

	public void setParamClazz(Class<?> paramClazz)
	{
		this.paramClazz = paramClazz;
	}

	public Class<?> getParamAnnotationClazz()
	{
		return paramAnnotationClazz;
	}

	public void setParamAnnotationClazz(Class<?> paramAnnotationClazz)
	{
		this.paramAnnotationClazz = paramAnnotationClazz;
	}

	public String getParamAnnotationValue()
	{
		return paramAnnotationValue;
	}

	public void setParamAnnotationValue(String paramAnnotationValue)
	{
		this.paramAnnotationValue = paramAnnotationValue;
	}

	public boolean isParamAnnotationRequired()
	{
		return paramAnnotationRequired;
	}

	public void setParamAnnotationRequired(boolean paramAnnotationRequired)
	{
		this.paramAnnotationRequired = paramAnnotationRequired;
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
