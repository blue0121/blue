package blue.core.tree;

/**
 * 树型结构
 * 
 * @author zhengj
 * @since 1.0 2016年6月9日
 */
public interface TreeItem
{
	/**
	 * 编号
	 * 
	 * @return 
	 */
	Integer getId();
	
	/**
	 * 上级编号
	 * 
	 * @return
	 */
	Integer getParentId();
	
}
