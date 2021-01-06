package uu.datamanagement.main.helper;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.utils.DocumentType;
import uu.datamanagement.main.utils.TimeInterval;

@Component
public class MetadataParserHelper {

  public final List<String> ALLOWED_PARAMETERS = Arrays.asList("DocumentType", "SenderIdentification", "ReceiverIdentification", "Domain", "CreationDateTime", "GSKTimeInterval");

  public void fillingMetadata(XMLStreamReader xmlStreamReader, Metadata metadata) {

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
        metadata.setTimeInterval(getTimeIntervalFromFile(xmlStreamReader.getAttributeValue(0)));
        break;
      default:
        break;
    }
  }

  private TimeInterval getTimeIntervalFromFile(String timeInterval) {
    String from = timeInterval.split("/")[0];
    String to = timeInterval.split("/")[1];
    return new TimeInterval(ZonedDateTime.parse(from), ZonedDateTime.parse(to));
  }
}
