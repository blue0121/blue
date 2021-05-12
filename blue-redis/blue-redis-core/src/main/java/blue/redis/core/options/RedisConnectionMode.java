package blue.redis.core.options;

/**
 * Redis 连接模式
 *
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public enum RedisConnectionMode {
	/**
	 * 单节点
	 */
	SINGLE,

	/**
	 * 哨兵
	 */
	SENTINEL,

	/**
	 * 集群
	 */
	CLUSTER,

}
