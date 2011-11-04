package com.github.mcs.stripes.navigation.factory;

import com.github.mcs.stripes.navigation.MenuItem;
import com.github.mcs.stripes.navigation.MenuRoot;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This factory allows to create a navigation menu via SQL connection.
 * <p>
 * It is expected that there is a table <tt>navigation</tt> which contains at least 
 * the columns <tt>id INT, parent_id INT, label VARCHAR(255)</tt>. Additional 
 * columns without any further configuration are allowed but will be ignored. 
 * Columns that are intended to be menu properties must be configured with a 
 * column-property map, where the key is the column name and the value is the 
 * name of the property.
 * 
 * @author Marcus Krassmann
 * @see #setConnection(java.sql.Connection) 
 * @see #setColumnMapping(java.util.Map) 
 */
public class SqlMenuFactory implements MenuFactory {

    private Connection connection;
    private Map<String, String> columnPropertyMap;

    /** 
     * Sets the database connection to use.
     * 
     * @param connection the SQL connection 
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Allows to map additional table columns to menu properties, like
     * <pre>("HREF_TARGET" => "hrefTarget")</pre>
     * @param columnPropertyMap a map containing column and property names
     */
    public void setColumnMapping(Map<String, String> columnPropertyMap) {
        this.columnPropertyMap = columnPropertyMap;
    }

    /** Intermediate class for table rows. */
    private static class Item {

        public String id;
        public String parent;
        public Map<String, String> props = new HashMap<String, String>();

        public Item(String id, String label, String parent) {
            this.id = id;
            this.parent = parent;
        }
    }

    @Override
    public MenuRoot build() throws MenuFactoryException {
        try {
            return parse();
        } catch (SQLException ex) {
            throw new MenuFactoryException("Error retrieving navigation from database", ex);
        }
    }

    private MenuRoot parse() throws SQLException {
        // Fetch all menu items from database and create intermediate objects.
        List<Item> items = new ArrayList<Item>();
        Map<String, MenuItem> menuItems = new HashMap<String, MenuItem>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM navigation");
        while (rs.next()) {
            // Fetch mandatory columns
            Item item = new Item(rs.getString("id"), rs.getString("label"), rs.getString("parent_id"));
            // Also retrieve additional columns, if they exist
            Map<String, String> customProperties = new HashMap<String, String>();
            for (Map.Entry<String, String> each : columnPropertyMap.entrySet()) {
                customProperties.put(each.getValue(), rs.getString(each.getKey()));
            }
            item.props = customProperties;
            items.add(item);
            // In this round, parent is always null. 
            // Gets populated in second rount
            menuItems.put(item.id, new MenuItem(item.id, null));
        }
        rs.close();
        stmt.close();

        // now create connection between parents and children
        MenuRoot root = new MenuRoot("menu");
        MenuItem current;
        for (Item item : items) {
            current = menuItems.get(item.id);
            if (item.parent == null) {
                current.setParent(root);
            } else {
                current.setParent(menuItems.get(item.parent));
            }
            // add custom properties
            current.setProperties(item.props);
        }
        // That's it - menu is initialized :-)
        return root;
    }
}
