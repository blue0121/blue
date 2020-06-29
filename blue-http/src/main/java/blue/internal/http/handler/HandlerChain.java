package blue.internal.http.handler;

import blue.core.util.AssertUtil;
import blue.http.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2020-01-05
 */
public class HandlerChain
{
	private static Logger logger = LoggerFactory.getLogger(HandlerChain.class);

	private Object handler;
	private List<Filter<Object, Object>> filterList = new ArrayList<>();
	private int filterIndex = -1;

	public HandlerChain(Object handler)
	{
		this(handler, null);
	}

	public HandlerChain(Object handler, List<Filter<Object, Object>> filterList)
	{
		AssertUtil.notNull(handler, "Handler");
		this.handler = handler;
		if (filterList != null && !filterList.isEmpty())
		{
			this.filterList.addAll(filterList);
		}
	}

	public boolean applyPreHandle(Object request, Object response) throws Exception
	{
		for (int i = 0; i < filterList.size(); i++)
		{
			Filter filter = filterList.get(i);
			if (!filter.preHandle(request, response))
			{
				this.triggerAfterCompletion(request, response, null);
				return false;
			}
			this.filterIndex = i;
		}
		return true;
	}

	public void applyPostHandle(Object request, Object response) throws Exception
	{
		for (int i = filterList.size() - 1; i >= 0; i--)
		{
			Filter filter = filterList.get(i);
			filter.postHandle(request, response);
		}
	}

	public void triggerAfterCompletion(Object request, Object response, Exception ex)
	{
		for (int i = filterIndex; i >= 0; i--)
		{
			Filter filter = filterList.get(i);
			try
			{
				filter.afterCompletion(request, response, ex);
			}
			catch (Exception e)
			{
				logger.error("Filter.afterCompletion throw exception,", e);
			}
		}
	}

	public Object getHandler()
	{
		return handler;
	}

	public void addFilter(Filter<Object, Object> filter)
	{
		this.filterList.add(filter);
	}

	public List<Filter<Object, Object>> getFilterList()
	{
		return List.copyOf(filterList);
	}

}
