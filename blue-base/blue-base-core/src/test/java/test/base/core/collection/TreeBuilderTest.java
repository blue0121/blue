package test.base.core.collection;

import blue.base.core.collection.TreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-20
 */
public class TreeBuilderTest {
	public TreeBuilderTest() {
	}

	@Test
	public void testBuildTree() {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu(null, 1));
        list.add(new Menu(-1, 2));
        list.add(new Menu(0, 3));
        list.add(new Menu(1, 4));
        list.add(new Menu(1, 5));
        list.add(new Menu(2, 6));

        Map<Integer, List<Menu>> tree = TreeBuilder.buildTree(list);
        Assertions.assertEquals(HashMap.class, tree.getClass());
        Assertions.assertEquals(3, tree.size());
        List<Menu> list0 = tree.get(0);
        Assertions.assertEquals(3, list0.size());
        Assertions.assertEquals(List.of(1,2,3), list0.stream().map(Menu::getId).collect(Collectors.toList()));
        List<Menu> list1 = tree.get(1);
        Assertions.assertEquals(2, list1.size());
        Assertions.assertEquals(List.of(4,5), list1.stream().map(Menu::getId).collect(Collectors.toList()));
        List<Menu> list2 = tree.get(2);
        Assertions.assertEquals(1, list2.size());
        Assertions.assertEquals(List.of(6), list2.stream().map(Menu::getId).collect(Collectors.toList()));
    }

}
