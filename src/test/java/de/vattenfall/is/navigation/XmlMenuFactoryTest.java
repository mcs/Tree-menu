package de.vattenfall.is.navigation;

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
    public void buildViaXmlStructure() {
        String xml = deliverXml();
        factory.setXml(xml);
        MenuRoot root = factory.build();
        
        assertEquals(2, root.getChildren().size());
        MenuItem child2 = root.getChildren().get(1);
        assertEquals("menu::child2", child2.getId());
        assertEquals("menu.entry2", child2.getLabel());

        assertEquals(3, child2.getChildren().size());
        MenuItem child5 = child2.getChildren().get(2);
        assertEquals("menu::child2::menu.entry5", child5.getId());
        assertEquals("menu.entry5", child5.getLabel());
    }

    private String deliverXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<menu>");
        xml.append("<item id=\"child1\" label=\"menu.entry1\" />");
        xml.append("<item id=\"child2\" label=\"menu.entry2\">");
        xml.append("<item id=\"child2_1\" label=\"menu.entry3\" />");
        xml.append("<item id=\"child2_2\" label=\"menu.entry4\" />");
        xml.append("<item label=\"menu.entry5\" />");
        xml.append("</item>");
        xml.append("</menu>");
        return xml.toString();
    }

}