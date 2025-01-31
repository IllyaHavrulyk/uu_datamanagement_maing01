package uu.datamanagement.main.abl;

import com.mongodb.WriteResult;
import javax.inject.Inject;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.api.dto.ClearStorageDtoIn;
import uu.datamanagement.main.api.dto.ClearStorageDtoOut;
import uu.datamanagement.main.api.exceptions.ClearStorageException.Error;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.helper.ValidationHelper;

@Component
public class ClearStorageAbl {

  private final MetadataDao metadataDao;
  private final GskDocumentDao gskDocumentDao;
  private final ValidationHelper validationHelper;

  @Inject
  public ClearStorageAbl(MetadataDao metadataDao, GskDocumentDao gskDocumentDao, ValidationHelper validationHelper) {
    this.metadataDao = metadataDao;
    this.gskDocumentDao = gskDocumentDao;
    this.validationHelper = validationHelper;
  }

  public ClearStorageDtoOut clean(String awid, ClearStorageDtoIn dtoIn) {
    // process/get check phase moderation, status will be OK

    validationHelper.validateDtoIn(dtoIn, Error.INVALID_DTO_IN);

    WriteResult resultMetadata;
    WriteResult resultGskDocument;

    resultMetadata = metadataDao.deleteMany(awid);
    resultGskDocument = gskDocumentDao.deleteMany(awid);

    ClearStorageDtoOut dtoOut = new ClearStorageDtoOut();
    dtoOut.setResultMetadata(resultMetadata);
    dtoOut.setResultGskDocument(resultGskDocument);
    return dtoOut;
  }

}
