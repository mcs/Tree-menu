package de.vattenfall.is.navigation;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation that creates a menu based on an existing XML structure.
 */
public class XmlMenuFactory implements MenuFactory {

    private static final Log log = LogFactory.getLog(XmlMenuFactory.class);
    private String xml;

    @Override
    public MenuRoot build() {
        try {
            return parse();
        }
        catch (XMLStreamException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("Provided XML stream invalid", e);
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
                if (elementName.equals("menu") && top == null) {
                    root = new MenuRoot("menu");
                    top = root; // push
                }
                else if (elementName.equals("item") && top != null) {
                    Map<String, String> map = new HashMap<String, String>();
                    Iterator<?> attributes = element.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = (Attribute) attributes.next();
                        map.put(attribute.getName().toString(), attribute.getValue());
                    }
                    top = new MenuItem(map.get("id"), map.get("label"), top); // push
                }
                else {
                    throw new RuntimeException("Unexpected XML element: " + elementName);
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
