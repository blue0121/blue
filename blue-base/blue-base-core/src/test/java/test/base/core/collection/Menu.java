package test.base.core.collection;

import blue.base.core.collection.TreeItem;

/**
 * @author Jin Zheng
 * @since 1.0 2021-04-20
 */
public class Menu implements TreeItem {
    private Integer parentId;
    private Integer id;
    private String name;

    public Menu(Integer parentId, Integer id) {
        this(parentId, id, "menu-" + id);
    }

    public Menu(Integer parentId, Integer id, String name) {
        this.parentId = parentId;
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(TreeItem o) {
        return Integer.compare(id, o.getId());
    }
}
