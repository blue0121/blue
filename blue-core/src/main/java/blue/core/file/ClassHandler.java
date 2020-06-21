package blue.core.file;

/**
 * 类处理器
 * 
 * @author zhengj
 * @since 1.0 2016年7月21日
 */
public interface ClassHandler
{
	/**
	 * 处理类
	 * 
	 * @param clazz 类型
	 */
	void handle(Class<?> clazz);
}
