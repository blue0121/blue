package blue.internal.jdbc.parser;

import blue.core.file.ClassHandler;
import blue.jdbc.annotation.Entity;
import blue.jdbc.annotation.Mapper;

/**
 * 扫描Jdbc类配置
 * 
 * @author zhengj
 * @since 1.0 2016年7月21日
 */
public class EntityClassHandler implements ClassHandler
{
	public EntityClassHandler()
	{
	}

	@Override
	public void handle(Class<?> clazz)
	{
		if (clazz.isAnnotationPresent(Entity.class) || clazz.isAnnotationPresent(Mapper.class))
		{
			Parser.getInstance().parse(clazz);
		}
		
	}
	
}
