package com.github.mcs.stripes.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItem {

    protected final String id;
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();
    private Map<String, String> properties = new HashMap<String, String>();

    protected MenuItem(String id) {
        this.id = id;
    }

    /**
     * Creates a new menu item with a label and a parent menu item.
     * Every menu item must have one and only one parent.
     * @param label the label (or label key if support for i18n is given)
     * @param parent the parent menu item
     */
    public MenuItem(String id, MenuItem parent) {
        this(id);
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public MenuItem getParent() {
        return parent;
    }

    /**
     * Allows to set a parent.
     * @param parent the parent item
     */
    public void setParent(MenuItem parent) {
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public String getId() {
        return String.format("%s::%s", parent.getId(), id);
    }

    public String getProperty(String prop) {
        return properties.get(prop);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> map) {
        properties = map;
    }
}
