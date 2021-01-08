package uu.datamanagement.main.abl;

import java.io.IOException;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;
import uu.datamanagement.main.api.exceptions.GSKDocumentRuntimeException.Error;
import uu.datamanagement.main.dao.GSKDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.helper.parser.GskDocumentParser;

@Component
public class GSKDocumentAbl {

  private final GSKDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final ModelMapper modelMapper;

  @Inject
  public GSKDocumentAbl(GSKDocumentDao gskDocumentDao, MetadataDao metadataDao, ValidationHelper validationHelper, ModelMapper modelMapper) {
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.modelMapper = modelMapper;
  }

  public GSKDocumentDtoOut create(String awid, GSKDocumentDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);
    GSKDocument document = new GSKDocument();
    Metadata metadata = new Metadata();
    try {
      GskDocumentParser documentParser = new GskDocumentParser();

      document = documentParser.process(dtoIn.getDocument().getInputStream());
      metadata = documentParser.getMetadata();
      document.setGskSeries(documentParser.getSeriesList());

    } catch (IOException e) {
      e.printStackTrace();
    }

    metadata.setFileName(dtoIn.getDocument().getOriginalFilename());
    metadata.setAwid(awid);
    Metadata savedMetadata = metadataDao.create(metadata);
    document.setMetadataId(savedMetadata.getId());
    document.setAwid(awid);
    gskDocumentDao.create(document);

    return modelMapper.map(document, GSKDocumentDtoOut.class);
  }

}
