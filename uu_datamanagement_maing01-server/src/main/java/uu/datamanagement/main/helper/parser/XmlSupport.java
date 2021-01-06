package uu.datamanagement.main.helper.parser;

import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

public class XmlSupport {

  private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

  public XmlSupport() {
  }

  public static XMLEventReader getXmlEventReader(InputStream inputStream) throws XMLStreamException {
    return XML_INPUT_FACTORY.createXMLEventReader(inputStream);
  }
}
