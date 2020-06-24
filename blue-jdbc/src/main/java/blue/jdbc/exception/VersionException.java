package blue.jdbc.exception;

/**
 * 版本异常
 * 
 * @author zhengj
 * @since 1.0 2016年9月10日
 */
public class VersionException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private Class<?> clazz;

	public VersionException(Class<?> clazz)
	{
		super(clazz.getName());
		this.clazz = clazz;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	@Override
	public String toString()
	{
		return clazz.getName() + " 版本过期";
	}
	
	
	
}
