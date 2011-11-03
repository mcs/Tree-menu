package com.hithub.mcs.stripes.navigation.factory;

import com.github.mcs.stripes.navigation.MenuRoot;
import com.github.mcs.stripes.navigation.factory.MenuFactoryException;
import com.github.mcs.stripes.navigation.factory.SqlMenuFactory;
import com.github.mcs.stripes.navigation.MenuItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SqlMenuFactoryTest {

    SqlMenuFactory factory;
    Connection connection;

    @BeforeClass
    public static void initJdbcDriver() throws ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
    }
    private Statement stmt;

    @Before
    public void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "sa", "");
        stmt = connection.createStatement();
        stmt.execute("CREATE TABLE navigation (id INT, parent_id INT, label VARCHAR(255), unknown_prop VARCHAR(255))");

        factory = new SqlMenuFactory();
        factory.setConnection(connection);
    }

    @After
    public void teardown() throws SQLException {
        stmt.execute("DROP TABLE navigation");
        stmt.close();
        connection.close();
    }

    @Test
    public void buildNav1() throws SQLException {
        // GIVEN
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label) VALUES (1, null, 'menu.entry1')");
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label) VALUES (2, null, 'menu.entry2')");
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label) VALUES (3, 2, 'menu.entry3')");
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label) VALUES (4, 2, 'menu.entry4')");
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label, unknown_prop) VALUES (5, 2, 'menu.entry5', 'testProp')");

        Map<String, String> columnPropertyMap = new HashMap<String, String>();
        columnPropertyMap.put("unknown_prop", "unknownProperty");

        // WHEN
        factory.setColumnMapping(columnPropertyMap);
        MenuRoot root = factory.build();

        // THEN
        assertEquals(2, root.getChildren().size());
        MenuItem child2 = root.getChildren().get(1);
        assertEquals("menu::2", child2.getId());
        assertEquals("menu.entry2", child2.getLabel());

        assertEquals(3, child2.getChildren().size());
        MenuItem child5 = child2.getChildren().get(2);
        assertEquals("menu::2::5", child5.getId());
        assertEquals("menu.entry5", child5.getLabel());
        assertEquals("testProp", child5.getProperty("unknownProperty"));
    }

    @Test(expected = MenuFactoryException.class)
    public void buildWithInvalidSqlTable() throws SQLException  {
        // GIVEN
        stmt.executeUpdate("INSERT INTO navigation (id, parent_id, label) VALUES (1, null, 'menu.entry1')");
        Map<String, String> columnPropertyMap = new HashMap<String, String>();
        columnPropertyMap.put("not_existing_prop", "illegalProperty");

        // WHEN
        factory.setColumnMapping(columnPropertyMap);
        factory.build();
        
        // THEN
        /* Exception */
    }
}