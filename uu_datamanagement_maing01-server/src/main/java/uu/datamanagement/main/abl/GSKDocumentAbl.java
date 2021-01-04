package uu.datamanagement.main.abl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;
import uu.datamanagement.main.api.exceptions.GSKDocumentRuntimeException.Error;
import uu.datamanagement.main.dao.GSKDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.MetadataParserHelper;
import uu.datamanagement.main.helper.ValidationHelper;

@Component
public class GSKDocumentAbl {

  private List<String> METADATA_PARAMETERS = Arrays.asList("DocumentType", "SenderIdentification", "ReceiverIdentification", "Domain", "CreationDateTime", "GSKTimeInterval");

  private final GSKDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final MetadataParserHelper metadataParserHelper;

  @Inject
  public GSKDocumentAbl(GSKDocumentDao gskDocumentDao, MetadataDao metadataDao, ValidationHelper validationHelper, MetadataParserHelper metadataParserHelper) {
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.metadataParserHelper = metadataParserHelper;
  }

  public GSKDocumentDtoOut create(String awid, GSKDocumentDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    GSKDocument gskDocument = new GSKDocument();
    Metadata metadata = new Metadata();
    metadata.setFileName(dtoIn.getDocument().getOriginalFilename());
    metadata.setAwid(awid);

    try {
      XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(dtoIn.getDocument().getInputStream());

      while (xmlStreamReader.hasNext()) {
        xmlStreamReader.next();
        if (xmlStreamReader.isStartElement()) {
          if (xmlStreamReader.getLocalName().equals("DocumentIdentification")) {
            gskDocument.setDocumentIdentification(xmlStreamReader.getAttributeValue(0));
          }

          if (METADATA_PARAMETERS.contains(xmlStreamReader.getLocalName())) {
            metadataParserHelper.fullMetadata(xmlStreamReader, metadata);
          }
        }
      }

    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String metadataId = metadataDao.create(metadata).getId();

    return new GSKDocumentDtoOut();
  }
}
