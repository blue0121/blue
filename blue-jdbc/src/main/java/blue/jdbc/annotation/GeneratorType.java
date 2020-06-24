package blue.jdbc.annotation;

/**
 * 主键产生类型
 * 
 * @author zhengj
 * @since 1.0 2011-4-1
 */
public enum GeneratorType
{
	/**
	 * <p>自动判断<p>
	 * <ol>
	 * <li>字段是字符串，用UUID方式</li>
	 * <li>字段是整型，用INCREMENT</li>
	 * <ol>
	 */
	AUTO,

	/**
	 * UUID 方式
	 */
	UUID,
	
	/**
	 * 自增长
	 */
	INCREMENT,
	
	/**
	 * 自行分配
	 */
	ASSIGNED
}
