package blue.jdbc.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库分页对象
 * 
 * @author zhengj
 * @date 2009-4-3 1.0
 */
public class Page implements Serializable
{
	private static final long serialVersionUID = -8111885037058285721L;
	
	/**
	 * 页面开始索引
	 */
	public static final int START = 1;
	
	/**
	 * 每页记录数
	 */
	public static final int SIZE = 20;

	/**
	 * 总记录数
	 */
	private int rows = 0;

	/**
	 * 每页记录数
	 */
	private int size;

	/**
	 * 总页数
	 */
	private int totalPage = 0;

	/**
	 * 当前页，从1开始
	 */
	private int curPage = 1;

	/**
	 * 当前记录位置，从0开始
	 */
	private int startRowNo = 0;

	/**
	 * 从数据库里查到的对象列表
	 */
	private List<?> objectList = new ArrayList<>();

	public Page()
	{
		this(SIZE);
	}
	
	public Page(int size)
	{
		this.size = size;
	}

	/**
	 * 设置总记录数
	 * 
	 * @param rows 总记录数
	 * @throws IllegalArgumentException 当总记录数小于 0 时抛出
	 */
	public void setRows(int rows) throws IllegalArgumentException
	{
		if (rows < 0)
			throw new IllegalArgumentException("总记录数不能小于 0");

		this.rows = rows;
		this.totalPage = (rows + size - 1) / size;
	}

	/**
	 * 设置当前页
	 * 
	 * @param curPage 当前页
	 * @throws IllegalArgumentException 当前页面能小于 1 时抛出
	 */
	public void setCurPage(int curPage) throws IllegalArgumentException
	{
		if (curPage < 1)
			throw new IllegalArgumentException("当前页面不能小于 1");

		this.curPage = curPage;
		this.startRowNo = size * (curPage - 1);
	}

	/**
	 * 取得总记录数
	 * 
	 * @return 总记录数
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * 取得每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * 取得总页数
	 * 
	 * @return 总页数
	 */
	public int getTotalPage()
	{
		return totalPage;
	}

	/**
	 * 取得当前记录位置，从0开始
	 * 
	 * @return 当前记录位置，从0开始
	 */
	public int getStartRowNo()
	{
		return startRowNo;
	}

	/**
	 * 取得当前页，从1开始
	 * 
	 * @return 当前页，从1开始
	 */
	public int getCurPage()
	{
		return curPage;
	}

	/**
	 * 取得从数据库里查到的对象列表
	 * 
	 * @return 对象列表
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjectList()
	{
		List<T> list = new ArrayList<>();
		for (Object object : objectList)
		{
			list.add((T)object);
		}
		return list;
	}

	/**
	 * 判断是否有下一页
	 *
	 * @return true表示有下一页，false表示最后一页
	 */
	public boolean hasNextPage()
	{
		return curPage < totalPage;
	}

	/**
	 * 判断是否有上一页
	 *
	 * @return true表示有上一页，false表示第一页
	 */
	public boolean hasPreviewPage()
	{
		return curPage > 1;
	}

	/**
	 * 设置从数据库里查到的对象列表
	 * 
	 * @param objectList 对象列表
	 */
	public void setObjectList(List<?> objectList)
	{
		this.objectList = objectList;
	}

	public void setSize(int size)
	{
		if (size < 1)
			throw new IllegalArgumentException("每页记录数不能小于 1");
		
		this.size = size;
		this.startRowNo = size * (curPage - 1);
	}

	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}

	public void setStartRowNo(int startRowNo)
	{
		this.startRowNo = startRowNo;
	}

}
