package uu.datamanagement.main.helper.parser;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import uu.datamanagement.main.api.exceptions.DeserializationException;
import uu.datamanagement.main.api.exceptions.DeserializationException.Error;

public abstract class AbstractXmlParser<T> {

  private static final String SLASH = "/";
  private Map<String, Consumer<XMLEvent>> eventList;

  protected abstract Map<String, Consumer<XMLEvent>> getEventList();

  protected abstract T getResult();

  public final T process(InputStream inputStream) {
    try {
      XMLEventReader reader = XmlSupport.getXmlEventReader(inputStream);
      return process(reader);
    } catch (XMLStreamException e) {
      throw new DeserializationException(Error.DESERIALIZATION_FAILED, e.getMessage());
    } finally {
      afterProcess();
    }
  }

  private T process(XMLEventReader reader) throws XMLStreamException {
    this.eventList = getEventList();
    processEvent(reader);
    return getResult();
  }

  private void processEvent(XMLEventReader reader) throws XMLStreamException {
    StringBuilder pathBuilder = new StringBuilder();
    String fullPath = "";
    while (reader.hasNext()) {
      final XMLEvent xmlEvent = reader.nextEvent();
      if (xmlEvent.isStartElement()) {
        String localName = xmlEvent.asStartElement().getName().getLocalPart();
        if (pathBuilder.length() > 0) {
          pathBuilder.append(SLASH);
        }
        pathBuilder.append(localName);
        fullPath = pathBuilder.toString();
        processStartElement(xmlEvent.asStartElement(), fullPath);
      } else if (xmlEvent.isCharacters()) {
        processCharactersElement(xmlEvent.asCharacters(), fullPath + "/text");
      } else if (xmlEvent.isEndElement()) {
        int slashIndex = pathBuilder.lastIndexOf(SLASH);
        pathBuilder.setLength(slashIndex == -1 ? 0 : slashIndex);
        fullPath = pathBuilder.toString();
      }
    }
  }

  private void processCharactersElement(Characters characters, String fullPath) {
    if (processableCharactersEvent(characters) && eventList.containsKey(fullPath)) {
      eventList.get(fullPath).accept(characters);
    }
  }

  private boolean processableCharactersEvent(Characters characters) {
    return !characters.getData().contains("\n");
  }

  private void processStartElement(StartElement xmlEvent, String fullPath) {
    if (eventList.containsKey(fullPath)) {
      eventList.get(fullPath).accept(xmlEvent);
    }
  }

  protected final String getV(XMLEvent xmlEvent) {
    return getAttr(xmlEvent, "v");
  }

  private String getAttr(XMLEvent xmlEvent, String attrName) {
    QName v = new QName(attrName);
    Attribute attribute = xmlEvent.asStartElement().getAttributeByName(v);
    return attribute != null ? attribute.getValue() : null;
  }

  private void afterProcess() {
  }

}
