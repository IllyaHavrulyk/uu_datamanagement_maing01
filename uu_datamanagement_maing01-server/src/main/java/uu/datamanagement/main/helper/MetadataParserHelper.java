package uu.datamanagement.main.helper;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.utils.DocumentType;

@Component
public class MetadataParserHelper {

  private final List<String> ALLOWED_PARAMETERS = Arrays.asList("DocumentType", "SenderIdentification", "ReceiverIdentification", "Domain", "CreationDateTime", "GSKTimeInterval");

  public void fullMetadata(XMLStreamReader xmlStreamReader, Metadata metadata) {

    switch (ALLOWED_PARAMETERS.indexOf(xmlStreamReader.getLocalName())) {
      case 0:
        metadata.setDocumentType(DocumentType.valueOf(xmlStreamReader.getAttributeValue(0)));
        break;
      case 1:
        metadata.setSender(xmlStreamReader.getAttributeValue(1));
        break;
      case 2:
        metadata.setReceiver(xmlStreamReader.getAttributeValue(1));
        break;
      case 3:
        metadata.setDomain(xmlStreamReader.getAttributeValue(1));
        break;
      case 4:
        metadata.setCreationDateTime(ZonedDateTime.parse(xmlStreamReader.getAttributeValue(0)));
        break;
      case 5:
        metadata.setTimeInterval(null);
        break;
      default:
        break;
    }

  }

}
