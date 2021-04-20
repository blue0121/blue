package blue.base.core.collection;

/**
 * 树型结构
 *
 * @author zhengj
 * @since 1.0 2016年6月9日
 */
public interface TreeItem extends Comparable<TreeItem> {
	/**
	 * 编号
	 *
	 * @return 必须大于0
	 */
	Integer getId();

	/**
	 * 上级编号
	 *
	 * @return 小于等于0或null
	 */
	Integer getParentId();

}
