package de.vattenfall.is.navigation;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    protected String id;
    private final String label;
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    protected MenuItem(String label) {
        this.label = label;
    }
    
    protected MenuItem(String id, String label) {
        this(label);
        this.id = id;
    }
    
    public MenuItem(String label, MenuItem parent) {
        this(label);
        this.parent = parent;
        parent.children.add(this);
    }

    public MenuItem(String id, String label, MenuItem parent) {
        this(label, parent);
        this.id = id;
    }

    public final String getLabel() {
        return label;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public MenuItem getParent() {
        return parent;
    }
    
    public String getId() {
        String exposedId = id == null ? label : id;
        return String.format("%s::%s", parent.getId(), exposedId);
    }
}
