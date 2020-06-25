package blue.internal.jdbc.core;

import blue.jdbc.core.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库分页对象
 * 
 * @author zhengj
 * @date 2009-4-3 1.0
 */
public class DefaultPage implements Page
{
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

	public DefaultPage()
	{
		this(ITEMS_PER_PAGE, PAGE_NUMBER);
	}
	
	public DefaultPage(int itemsPerPage, int pageNumber)
	{
		this.setItemsPerPage(itemsPerPage);
		this.setPageNumber(pageNumber);
	}

	@Override
	public void setTotalResult(int totalResult) throws IllegalArgumentException
	{
		if (totalResult < 0)
			throw new IllegalArgumentException("总记录数不能小于 0");

		this.totalResult = totalResult;
		this.totalPage = (totalResult + itemsPerPage - 1) / itemsPerPage;
	}

	@Override
	public void setPageNumber(int pageNumber) throws IllegalArgumentException
	{
		if (pageNumber < 1)
			throw new IllegalArgumentException("当前页面不能小于 1");

		this.pageNumber = pageNumber;
		this.rowIndex = itemsPerPage * (pageNumber - 1);
	}

	@Override
	public int getTotalResult()
	{
		return totalResult;
	}

	@Override
	public int getItemsPerPage()
	{
		return itemsPerPage;
	}

	@Override
	public int getTotalPage()
	{
		return totalPage;
	}

	@Override
	public int getRowIndex()
	{
		return rowIndex;
	}

	@Override
	public int getPageNumber()
	{
		return pageNumber;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getResults()
	{
		List<T> list = new ArrayList<>();
		for (Object object : results)
		{
			list.add((T)object);
		}
		return list;
	}

	@Override
	public boolean hasNextPage()
	{
		return pageNumber < totalPage;
	}

	@Override
	public boolean hasPreviewPage()
	{
		return pageNumber > 1;
	}

	@Override
	public void setResults(List<?> objectList)
	{
		this.results = objectList;
	}

	@Override
	public void setItemsPerPage(int itemsPerPage)
	{
		if (itemsPerPage < 1)
			throw new IllegalArgumentException("每页记录数不能小于 1");
		
		this.itemsPerPage = itemsPerPage;
		this.rowIndex = itemsPerPage * (pageNumber - 1);
	}

}
