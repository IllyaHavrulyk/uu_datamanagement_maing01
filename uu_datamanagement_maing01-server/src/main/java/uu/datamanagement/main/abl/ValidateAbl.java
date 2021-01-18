package uu.datamanagement.main.abl;

import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;
import org.springframework.stereotype.Component;
import uu.app.datastore.domain.PagedResult;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.validation.DocumentValidationHelper;
import uu.datamanagement.main.validation.ValidationResult;

@Component
public class ValidateAbl {

  private final DocumentValidationHelper documentValidationHelper;
  private final GskDocumentDao gskDocumentDao;
  private final MetadataDao metadataDao;

  @Inject
  public ValidateAbl(DocumentValidationHelper documentValidationHelper, GskDocumentDao gskDocumentDao, MetadataDao metadataDao) {
    this.documentValidationHelper = documentValidationHelper;
    this.gskDocumentDao = gskDocumentDao;
    this.metadataDao = metadataDao;
  }

  public ValidationResult validate(String awid) {
    ValidationResult validationResult = ValidationResult.success();

    PagedResult<Metadata> metadataPagedResult = metadataDao.listWithEmptyValidField(awid);

    for (Metadata metadata : metadataPagedResult.getItemList()) {
      GskDocument gskDocument = gskDocumentDao.getByMetadataId(awid, metadata.getId());

      validationResult.merge(runValidationProcessForOneDocument(gskDocument, metadata));

      metadataDao.update(metadata);

      simulatedDelay();
    }

    return validationResult;
  }

  private ValidationResult runValidationProcessForOneDocument(GskDocument gskDocument, Metadata metadata) {
    ValidationResult validationResult = ValidationResult.success();

    validationResult.merge(documentValidationHelper.allTimeIntervalEqualToGskTimeInterval(gskDocument, metadata));

    validationResult.merge(documentValidationHelper.oneBlockListPresentInFile(gskDocument));

    validationResult.merge(documentValidationHelper.nodeNameAlwaysPresent(gskDocument));

    validationResult.merge(documentValidationHelper.checkAreaCodingName(gskDocument));

    validationResult.merge(documentValidationHelper.eachBlockContainNodes(gskDocument));

    validationResult.merge(documentValidationHelper.timeSeriesIdIsSequential(gskDocument));

    metadata.setValid(validationResult.getValidationMessages().isEmpty());
    metadata.setValidationResult(validationResult);
    return validationResult;
  }

  private void simulatedDelay() {
    try {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      Thread.sleep(random.nextInt(1000, 10000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
