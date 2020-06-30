package blue.internal.http.config;

import blue.core.util.AssertUtil;
import blue.core.util.StringUtil;
import blue.http.exception.HttpServerException;
import blue.http.filter.Filter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2020-01-05
 */
public class FilterConfig implements InitializingBean, Comparable<FilterConfig>
{
	private Class<?> clazz;
	private Filter<Object, Object> object;
	private int order = 10;
	private List<String> filterList = new ArrayList<>();
	private List<String> excludeList = new ArrayList<>();

	public FilterConfig()
	{
	}

	public void setObject(Object object)
	{
		AssertUtil.notNull(object, "Object");
		if (!(object instanceof Filter))
			throw new HttpServerException(object.getClass().getName() + " 没有实现 Filter 接口");

		this.object = (Filter<Object, Object>) object;
		this.clazz = this.object.getClass();
	}

	public void setFilters(String filters)
	{
		AssertUtil.notEmpty(filters, "Filter url");
		for (String filter : StringUtil.split(filters))
		{
			filterList.add(filter.trim());
		}
	}

	public void setExcludes(String excludes)
	{
		if (excludes == null || excludes.isEmpty())
			return;

		for (String exclude : StringUtil.split(excludes))
		{
			excludeList.add(exclude.trim());
		}
	}

	@Override
	public int compareTo(FilterConfig o)
	{
		return order - o.order;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (object == null)
			throw new HttpServerException("InvokerFilter 实例不能为空");

		if (filterList == null || filterList.isEmpty())
			throw new HttpServerException("过滤URL不能为空");
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public Filter<Object, Object> getObject()
	{
		return object;
	}

	public List<String> getFilterList()
	{
		return filterList;
	}

	public List<String> getExcludeList()
	{
		return excludeList;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}
}
