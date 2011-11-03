package com.github.mcs.stripes.navigation.factory;

import com.github.mcs.stripes.navigation.MenuItem;
import com.github.mcs.stripes.navigation.MenuRoot;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.EventFilter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Creates a navigation menu based on an existing XML structure.
 * An example self-explaining menu:
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;menu&gt;
 *   &lt;item id="child1" label="menu.entry1" /&gt;
 *   &lt;item id="child2" label="menu.entry2"&gt;
 *     &lt;item id="child2_1" label="menu.entry3" /&gt;
 *     &lt;item id="child2_2" label="menu.entry4" /&gt;
 *     &lt;item label="menu.entry5" /&gt;
 *   &lt;/item&gt;
 * &lt;/menu&gt;
 * </pre>
 */
public class XmlMenuFactory implements MenuFactory {

  private String xml;

  @Override
  public MenuRoot build() {
    try {
      return parse();
    } catch (XMLStreamException e) {
      throw new MenuFactoryException("Provided XML stream is invalid", e);
    } catch (RuntimeException e) {
      throw new MenuFactoryException(e.getMessage(), e);
    }
  }

  /**
   * Provides the XML menu structure to this factory.
   * @param xml the XML representation of the menu
   */
  public void setXml(String xml) {
    this.xml = xml;
  }

  private MenuRoot parse() throws FactoryConfigurationError, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader unfilteredParser = factory.createXMLEventReader(new StringReader(xml));
    XMLEventReader parser = factory.createFilteredReader(unfilteredParser, new EventFilter() {
      public boolean accept(XMLEvent event) {
        return !event.isCharacters();
      }
    });
    MenuRoot root = null;
    MenuItem top = null;
    while (parser.hasNext()) {
      XMLEvent event = parser.nextEvent();
      int eventType = event.getEventType();
      switch (eventType) {
        case XMLStreamConstants.START_ELEMENT:
          StartElement element = event.asStartElement();
          String elementName = element.getName().getLocalPart();
          if ("menu".equals(elementName) && top == null) {
            root = new MenuRoot("menu");
            top = root; // push
          } else if ("item".equals(elementName) && top != null) {
            Map<String, String> map = new HashMap<String, String>();
            Iterator<?> attributes = element.getAttributes();
            while (attributes.hasNext()) {
              Attribute attribute = (Attribute) attributes.next();
              map.put(attribute.getName().toString(), attribute.getValue());
            }
            top = new MenuItem(map.remove("id"), map.remove("label"), top); // push
            top.setProperties(map);
          } else {
            throw new XMLStreamException("Unexpected XML element: " + elementName);
          }
          break;
        case XMLStreamConstants.END_ELEMENT:
          top = top.getParent(); // pop
          break;
        case XMLStreamConstants.END_DOCUMENT:
          parser.close();
          break;
      }
    }
    return root;
  }
}
