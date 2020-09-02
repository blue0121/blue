package blue.core.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库分页对象
 *
 * @author Jin Zheng
 * @since 1.0 2020-09-02
 */
public class Page
{
	/**
	 * 页面开始索引
	 */
	public static final int PAGE_NUMBER = 1;

	/**
	 * 每页记录数
	 */
	public static final int ITEMS_PER_PAGE = 20;

	/**
	 * 总记录数
	 */
	private int totalResult = 0;

	/**
	 * 每页记录数
	 */
	private int itemsPerPage = ITEMS_PER_PAGE;

	/**
	 * 总页数
	 */
	private int totalPage = 0;

	/**
	 * 当前页，从1开始
	 */
	private int pageNumber = PAGE_NUMBER;

	/**
	 * 当前记录位置，从0开始
	 */
	private int rowIndex = 0;

	/**
	 * 从数据库里查到的对象列表
	 */
	private List<?> results = new ArrayList<>();

	public Page()
	{
		this(ITEMS_PER_PAGE, PAGE_NUMBER);
	}

	public Page(int itemsPerPage, int pageNumber)
	{
		this.setItemsPerPage(itemsPerPage);
		this.setPageNumber(pageNumber);
	}

	public void setTotalResult(int totalResult) throws IllegalArgumentException
	{
		if (totalResult < 0)
			throw new IllegalArgumentException("总记录数不能小于 0");

		this.totalResult = totalResult;
		this.totalPage = (totalResult + itemsPerPage - 1) / itemsPerPage;
	}

	public void setPageNumber(int pageNumber) throws IllegalArgumentException
	{
		if (pageNumber < 1)
			throw new IllegalArgumentException("当前页面不能小于 1");

		this.pageNumber = pageNumber;
		this.rowIndex = itemsPerPage * (pageNumber - 1);
	}

	public int getTotalResult()
	{
		return totalResult;
	}

	public int getItemsPerPage()
	{
		return itemsPerPage;
	}

	public int getTotalPage()
	{
		return totalPage;
	}

	public int getRowIndex()
	{
		return rowIndex;
	}

	public int getPageNumber()
	{
		return pageNumber;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getResults()
	{
		List<T> list = new ArrayList<>();
		for (Object object : results)
		{
			list.add((T)object);
		}
		return list;
	}

	public boolean hasNextPage()
	{
		return pageNumber < totalPage;
	}

	public boolean hasPreviewPage()
	{
		return pageNumber > 1;
	}

	public void setResults(List<?> objectList)
	{
		this.results = objectList;
	}

	public void setItemsPerPage(int itemsPerPage)
	{
		if (itemsPerPage < 1)
			throw new IllegalArgumentException("每页记录数不能小于 1");

		this.itemsPerPage = itemsPerPage;
		this.rowIndex = itemsPerPage * (pageNumber - 1);
	}

}
