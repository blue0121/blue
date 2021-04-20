package blue.base.core.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 树型结构
 *
 * @author zhengj
 * @since 1.0 2016年6月9日
 */
public class TreeBuilder {
	private static final Integer ZERO = 0;

	private TreeBuilder() {
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<Integer, List<T>> buildTree(List<T> list) {
		return buildTree(list, false);
	}

	/**
	 * 把 List 变成 Map<parent, List<T>> 结构
	 *
	 * @param list
	 * @param sort
	 * @param <T>
	 * @return
	 */
	public static <T extends TreeItem> Map<Integer, List<T>> buildTree(List<T> list, boolean sort) {
		Map<Integer, List<T>> map = sort ? new TreeMap<>() : new HashMap<>();
		if (list == null || list.isEmpty()) {
			return map;
		}

		if (sort) {
			Collections.sort(list);
		}
		for (T t : list) {
			Integer parentId = getParentId(t.getParentId());
			List<T> tmp = map.computeIfAbsent(parentId, k -> new ArrayList<>());
			tmp.add(t);
		}

		return map;
	}

	private static Integer getParentId(Integer parentId) {
		if (parentId == null || parentId < ZERO) {
			return ZERO;
		}
		return parentId;
	}

}
