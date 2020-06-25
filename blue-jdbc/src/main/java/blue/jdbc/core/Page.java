package blue.jdbc.core;

import blue.internal.jdbc.core.DefaultPage;

import java.util.List;

/**
 * 数据库分页对象
 * 
 * @author zhengj
 * @date 2009-4-3 1.0
 */
public interface Page
{
	/**
	 * 页面开始索引
	 */
	int PAGE_NUMBER = 1;
	
	/**
	 * 每页记录数
	 */
	int ITEMS_PER_PAGE = 20;

	/**
	 * 创建分页对象
	 *
	 * @param itemsPerPage 每页记录数
	 * @param pageNumber 开始页面
	 * @return 分页对象
	 */
	static Page newPage(int itemsPerPage, int pageNumber)
	{
		return new DefaultPage(itemsPerPage, pageNumber);
	}

	/**
	 * 设置总记录数
	 * 
	 * @param totalResult 总记录数
	 * @throws IllegalArgumentException 当总记录数小于 0 时抛出
	 */
	void setTotalResult(int totalResult) throws IllegalArgumentException;

	/**
	 * 设置当前页，从1开始
	 * 
	 * @param pageNumber 当前页
	 * @throws IllegalArgumentException 当前页面能小于 1 时抛出
	 */
	void setPageNumber(int pageNumber) throws IllegalArgumentException;

	/**
	 * 取得总记录数
	 * 
	 * @return 总记录数
	 */
	int getTotalResult();

	/**
	 * 取得每页记录数
	 * 
	 * @return 每页记录数
	 */
	int getItemsPerPage();

	/**
	 * 取得总页数
	 * 
	 * @return 总页数
	 */
	int getTotalPage();

	/**
	 * 取得当前记录位置，从0开始
	 * 
	 * @return 当前记录位置，从0开始
	 */
	int getRowIndex();

	/**
	 * 取得当前页，从1开始
	 * 
	 * @return 当前页，从1开始
	 */
	int getPageNumber();

	/**
	 * 取得从数据库里查到的对象列表
	 * 
	 * @return 对象列表
	 */
	<T> List<T> getResults();

	/**
	 * 判断是否有下一页
	 *
	 * @return true表示有下一页，false表示最后一页
	 */
	boolean hasNextPage();

	/**
	 * 判断是否有上一页
	 *
	 * @return true表示有上一页，false表示第一页
	 */
	boolean hasPreviewPage();

	/**
	 * 设置从数据库里查到的对象列表
	 * 
	 * @param results 对象列表
	 */
	void setResults(List<?> results);

	void setItemsPerPage(int itemsPerPage);

}
