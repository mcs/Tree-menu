package com.hithub.mcs.stripes.navigation.factory;

import com.github.mcs.stripes.navigation.MenuRoot;
import com.github.mcs.stripes.navigation.factory.MenuFactoryException;
import com.github.mcs.stripes.navigation.factory.XmlMenuFactory;
import com.github.mcs.stripes.navigation.MenuItem;
import com.github.mcs.util.FileUtil;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class XmlMenuFactoryTest {

    private XmlMenuFactory factory;

    @Before
    public void init() {
        factory = new XmlMenuFactory();
    }

    @Test
    public void buildWithValidXml() throws Exception {
        // GIVEN
        String xml = FileUtil.readXmlFromFile("nav1.xml");
        factory.setXml(xml);

        // WHEN
        MenuRoot root = factory.build();

        // THEN
        assertEquals(2, root.getChildren().size());
        MenuItem child2 = root.getChildren().get(1);
        assertEquals("menu::child2", child2.getId());
        assertEquals("menu.entry2", child2.getProperty("label"));

        assertEquals(3, child2.getChildren().size());
        MenuItem child5 = child2.getChildren().get(2);
        assertEquals("menu::child2::menu.entry5", child5.getId());
        assertEquals("menu.entry5", child5.getProperty("label"));
        assertEquals("testProp", child5.getProperty("unknownProperty"));
    }

    @Test(expected = MenuFactoryException.class)
    public void buildWithInvalidXml() throws Exception {
        String xml = FileUtil.readXmlFromFile("navUnknownElement.xml");
        factory.setXml(xml);
        factory.build();
    }
}