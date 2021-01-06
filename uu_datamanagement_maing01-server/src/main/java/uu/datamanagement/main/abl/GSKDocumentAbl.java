package uu.datamanagement.main.abl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.GSKSeries;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;
import uu.datamanagement.main.api.exceptions.GSKDocumentRuntimeException.Error;
import uu.datamanagement.main.dao.GSKDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.GSKBlockHelper;
import uu.datamanagement.main.helper.GSKSeriesParserHelper;
import uu.datamanagement.main.helper.MetadataParserHelper;
import uu.datamanagement.main.helper.ValidationHelper;

@Component
public class GSKDocumentAbl {

  private final GSKDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final MetadataParserHelper metadataParserHelper;
  private final GSKSeriesParserHelper gskSeriesParserHelper;
  private final GSKBlockHelper gskBrockHelper;

  @Inject
  public GSKDocumentAbl(GSKDocumentDao gskDocumentDao, MetadataDao metadataDao, ValidationHelper validationHelper, MetadataParserHelper metadataParserHelper,
    GSKSeriesParserHelper gskSeriesParserHelper, GSKBlockHelper gskBrockHelper) {
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.metadataParserHelper = metadataParserHelper;
    this.gskSeriesParserHelper = gskSeriesParserHelper;
    this.gskBrockHelper = gskBrockHelper;
  }

  public GSKDocumentDtoOut create(String awid, GSKDocumentDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    GSKDocument gskDocument = new GSKDocument();
    Metadata metadata = new Metadata();
    GSKSeries gskSeries = new GSKSeries();
    List<GSKSeries> gskSeriesList = new ArrayList<>();
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

          if (metadataParserHelper.ALLOWED_PARAMETERS.contains(xmlStreamReader.getLocalName())) {
            metadataParserHelper.fillingMetadata(xmlStreamReader, metadata);
          }

          if (xmlStreamReader.getLocalName().equals("GSKSeries")) {
            gskSeries = new GSKSeries();
          }

          if (gskSeriesParserHelper.ALLOWED_PARAMETERS.contains(xmlStreamReader.getLocalName())) {
            gskSeriesParserHelper.fillingGSKSeries(xmlStreamReader, gskSeries, gskSeriesList);
          }

        }
      }

    } catch (XMLStreamException | IOException e) {
      e.printStackTrace();
    }

    Metadata metadataId = metadataDao.create(metadata);
    gskDocument.setMetadataId(metadataId.getId());
    gskDocument.setGskSeries(gskSeriesList);
    gskDocument.setAwid(awid);

    gskDocumentDao.create(gskDocument);

    return new GSKDocumentDtoOut();
  }

}
