package uu.datamanagement.main.abl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PagedResult;
import uu.app.server.dto.ContentDispositionType;
import uu.app.server.dto.DownloadableResourceDtoOut;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoIn;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoOut;
import uu.datamanagement.main.api.dto.GskDoumentExportDtoIn;
import uu.datamanagement.main.api.exceptions.GskDocumentCreateException;
import uu.datamanagement.main.api.exceptions.GskDocumentExportException;
import uu.datamanagement.main.api.exceptions.GskDocumentExportException.Error;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.helper.exception.GskDocumentParserException;
import uu.datamanagement.main.helper.parser.GskDocumentParser;
import uu.datamanagement.main.serde.GskDocumentBuilder;
import uu.datamanagement.main.serde.exception.GskDocumentBuilderException;

@Component
public class GskDocumentAbl {

  private final GskDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;
  private final ValidationHelper validationHelper;
  private final ModelMapper modelMapper;
  private final GskDocumentBuilder gskDocumentBuilder;

  @Inject
  public GskDocumentAbl(GskDocumentDao gskDocumentDao, MetadataDao metadataDao, ValidationHelper validationHelper, ModelMapper modelMapper,
    GskDocumentBuilder gskDocumentBuilder) {
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
    this.validationHelper = validationHelper;
    this.modelMapper = modelMapper;
    this.gskDocumentBuilder = gskDocumentBuilder;
  }

  public GskDocumentCreateDtoOut create(String awid, GskDocumentCreateDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, GskDocumentCreateException.Error.INVALID_DTO_IN);
    GskDocument document;
    Metadata metadata;
    try {
      GskDocumentParser documentParser = new GskDocumentParser();

      document = documentParser.process(dtoIn.getDocument().getInputStream());
      metadata = documentParser.getMetadata();
      document.setGskSeries(documentParser.getSeriesList());
    } catch (GskDocumentParserException | IOException e) {
      throw new GskDocumentCreateException(GskDocumentCreateException.Error.GET_INPUT_STREAM_FAILED);
    }

    metadata.setFileName(dtoIn.getDocument().getOriginalFilename());
    metadata.setAwid(awid);
    Metadata savedMetadata;
    try {
      savedMetadata = metadataDao.create(metadata);
    } catch (GskDocumentCreateException e) {
      throw new GskDocumentCreateException(GskDocumentCreateException.Error.CREATE_METADATA_FAILED, e);
    }
    document.setMetadataId(savedMetadata.getId());
    document.setAwid(awid);
    GskDocument createdDocument;
    try {
      createdDocument = gskDocumentDao.create(document);
    } catch (GskDocumentCreateException e) {
      throw new GskDocumentCreateException(GskDocumentCreateException.Error.CREATE_GSK_DOCUMENT_FAILED, e);
    }

    return modelMapper.map(createdDocument, GskDocumentCreateDtoOut.class);
  }

  public DownloadableResourceDtoOut export(String awid, GskDoumentExportDtoIn dtoIn) {
    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    PagedResult<GskDocument> pagedResult = gskDocumentDao.list(awid, dtoIn.getPageInfo());
    List<GskDocument> gskDocuments = pagedResult.getItemList();

    byte[] result = generateZipArchive(awid, gskDocuments);
    String filename = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";
    return createDtoOut(filename, result);
  }

  private byte[] generateZipArchive(String awid, List<GskDocument> gskDocuments) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try (ZipOutputStream zos = new ZipOutputStream(baos);) {
      for (GskDocument gskDocument : gskDocuments) {
        Metadata metadata = metadataDao.getById(awid, gskDocument.getMetadataId())
          .orElseThrow(() -> new GskDocumentExportException(GskDocumentExportException.Error.GET_METADATA_FAILED, Collections.singletonMap("id", gskDocument.getMetadataId())));

        byte[] bytes = gskDocumentBuilder.build(gskDocument, metadata);

        ZipEntry zipEntry = new ZipEntry(metadata.getFileName());
        zipEntry.setSize(bytes.length);
        zos.putNextEntry(zipEntry);
        zos.write(bytes);
        zos.closeEntry();
      }
    } catch (GskDocumentBuilderException | IOException e) {
      throw new GskDocumentExportException(Error.BUILD_DOCUMENT_FAILED, e);
    }
    return baos.toByteArray();
  }

  private DownloadableResourceDtoOut createDtoOut(String filename, byte[] bytesXml) {
    return new DownloadableResourceDtoOut(
      new ByteArrayInputStream(bytesXml),
      filename,
      (long) bytesXml.length,
      ContentDispositionType.INLINE,
      MediaType.MULTIPART_FORM_DATA
    );
  }

}
