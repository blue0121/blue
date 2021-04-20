package blue.base.core.id;

/**
 * @author Jin Zheng
 * @since 1.0 2020-06-28
 */
public interface SnowflakeId
{

	/**
	 * 产生ID
	 * @return
	 */
	long nextId();

	/**
	 * 根据解析SnowflakeId解析出元数据
	 *
	 * @param id SnowflakeId
	 * @return Id元数据
	 */
	Metadata getMetadata(long id);

}
