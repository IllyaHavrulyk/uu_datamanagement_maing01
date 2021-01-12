package uu.datamanagement.main.abl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PagedResult;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;
import uu.datamanagement.main.api.dto.GSKDocumentExportDtoOut;
import uu.datamanagement.main.api.dto.GSKDoumentExportDtoIn;
import uu.datamanagement.main.api.exceptions.GSKDocumentRuntimeException.Error;
import uu.datamanagement.main.api.exceptions.MetadataRuntimeException;
import uu.datamanagement.main.dao.GSKDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.helper.parser.GskDocumentParser;
import uu.datamanagement.main.serde.GSKDocumentBuilder;

@Component
public class GSKDocumentAbl {

  private final GSKDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final ModelMapper modelMapper;
  private final GSKDocumentBuilder gskDocumentBuilder;

  @Inject
  public GSKDocumentAbl(GSKDocumentDao gskDocumentDao, MetadataDao metadataDao, ValidationHelper validationHelper, ModelMapper modelMapper,
    GSKDocumentBuilder gskDocumentBuilder) {
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.modelMapper = modelMapper;
    this.gskDocumentBuilder = gskDocumentBuilder;
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
    GSKDocument createdDocument = gskDocumentDao.create(document);

    GSKDocumentDtoOut dtoOut = new GSKDocumentDtoOut();
    dtoOut.setId(createdDocument.getId());
    dtoOut.setMetadataId(document.getMetadataId());

    return dtoOut;
  }

  public GSKDocumentExportDtoOut export(String awid, GSKDoumentExportDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    PagedResult<GSKDocument> pagedResult = gskDocumentDao.list(awid, dtoIn.getPageInfo());
    List<GSKDocument> gskDocuments = pagedResult.getItemList();

    byte[] result = new byte[0];
    try {
      result = generateZipArchive(awid, gskDocuments);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      FileOutputStream fos = new FileOutputStream("result.zip");
      fos.write(result);
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    GSKDocumentExportDtoOut dtoOut = new GSKDocumentExportDtoOut();
    dtoOut.setBytes(result);
    return dtoOut;
  }

  private byte[] generateZipArchive(String awid, List<GSKDocument> gskDocuments) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    for (GSKDocument gskDocument : gskDocuments) {
      Metadata metadata = metadataDao.getById(awid, gskDocument.getMetadataId())
        .orElseThrow(() -> new MetadataRuntimeException(MetadataRuntimeException.Error.GET_METADATA_FAILED, Collections.singletonMap("id", gskDocument.getMetadataId())));

      byte[] bytes = gskDocumentBuilder.build(gskDocument, metadata);

      ZipEntry zipEntry = new ZipEntry(metadata.getFileName());
      zipEntry.setSize(bytes.length);
      zos.putNextEntry(zipEntry);
      zos.write(bytes);
      zos.closeEntry();
    }

    zos.close();

    return baos.toByteArray();
  }

}
