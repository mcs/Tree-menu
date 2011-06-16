package de.vattenfall.is.navigation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
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
    String xml = deliverXmlFromFile("nav1.xml");
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
    assertEquals("testProp", child5.getProperty("unknownProperty"));
  }
  
  @Test(expected = MenuFactoryException.class)
  public void buildWithInvalidXml() throws Exception {
    String xml = deliverXmlFromFile("navUnknownElement.xml");
    factory.setXml(xml);
    factory.build();
  }

  private String deliverXmlFromFile(String filename) throws Exception {
    StringBuilder result = new StringBuilder();
    URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);
    BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
    while (reader.ready()) {
      result.append(reader.readLine()).append('\n');
    }
    reader.close();
    return result.toString();
  }
}