package blue.core.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TreeBuilder
{
	private TreeBuilder()
	{
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构，FreeMarker专用
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<String, List<T>> buildMenu(List<T> list)
	{
		return buildMenu(list, false);
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构，FreeMarker专用
	 * @param list
	 * @param sort
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<String, List<T>> buildMenu(List<T> list, boolean sort)
	{
		Map<String, List<T>> map = sort ? new TreeMap<>() : new HashMap<>();
		if (list == null || list.isEmpty())
			return map;
		
		for (T t : list)
		{
			String parentId = t.getParentId() == null ? "0" : String.valueOf(t.getParentId());
			List<T> tmp = map.get(parentId);
			if (tmp == null)
			{
				tmp = new ArrayList<>();
				map.put(parentId, tmp);
			}
			tmp.add(t);
		}
		
		return map;
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<Integer, List<T>> buildTree(List<T> list)
	{
		return buildTree(list, false);
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构
	 * @param list
	 * @param sort
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<Integer, List<T>> buildTree(List<T> list, boolean sort)
	{
		Map<Integer, List<T>> map = sort ? new TreeMap<>() : new HashMap<>();
		if (list == null || list.isEmpty())
			return map;

		for (T t : list)
		{
			Integer parentId = t.getParentId() == null ? 0 : t.getParentId();
			List<T> tmp = map.get(parentId);
			if (tmp == null)
			{
				tmp = new ArrayList<>();
				map.put(parentId, tmp);
			}
			tmp.add(t);
		}

		return map;
	}
	
}
