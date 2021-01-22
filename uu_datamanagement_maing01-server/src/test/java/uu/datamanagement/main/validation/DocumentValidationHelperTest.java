package uu.datamanagement.main.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.GskDocumentMongoDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.parser.GskDocumentParser;
import uu.datamanagement.main.rules.ClearDatabaseRule;
import uu.datamanagement.main.validation.DocumentValidationHelperTest.DocumentValidationHelperConfiguration;
import uu.datamanagement.main.validation.exception.DocumentValidationException.Error;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
  SubAppPersistenceConfiguration.class,
  DocumentValidationHelperConfiguration.class
})
public class DocumentValidationHelperTest {

  private final Map<String, BiConsumer<GskDocument, ValidationResult>> keyMap = Stream.of(
    new AbstractMap.SimpleEntry<String, BiConsumer<GskDocument, ValidationResult>>("oneBlockListPresentInFile", (g, v) -> validationResultForOneBlockTest(v, g)),
    new AbstractMap.SimpleEntry<String, BiConsumer<GskDocument, ValidationResult>>("checkAreaCodingName", (g, v) -> validationResultForCheckAreaCodingName(v, g)),
    new AbstractMap.SimpleEntry<String, BiConsumer<GskDocument, ValidationResult>>("nodeNameAlwaysPresent", (g, v) -> validationResultForNodeNameAlwaysPresent(v, g)),
    new AbstractMap.SimpleEntry<String, BiConsumer<GskDocument, ValidationResult>>("timeSeriesIdIsSequential", (g, v) -> validationResultForTimeSeriesIdIsSequential(v, g)),
    new AbstractMap.SimpleEntry<String, BiConsumer<GskDocument, ValidationResult>>("eachBlockContainNodes", (g, v) -> validationResultForEachBlockContainNodes(v, g))
  ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


  @Autowired
  @Rule
  public ClearDatabaseRule clearDatabaseRule;

  @Inject
  private MetadataDao metadataDao;

  @Inject
  private GskDocumentDao gskDocumentDao;

  @Inject
  private DocumentValidationHelper documentValidationHelper;

  @Test
  public void testAllTimeIntervalEqualToGskTimeInterval() {
    prepareDataForTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"));
    ValidationResult validationResult = ValidationResult.success();
    List<Metadata> metadataList = metadataDao.listWithEmptyValidField(clearDatabaseRule.getAwid()).getItemList();

    for (Metadata metadata : metadataList) {
      GskDocument gskDocument = gskDocumentDao.getByMetadataId(clearDatabaseRule.getAwid(), metadata.getId());
      validationResult.merge(documentValidationHelper.allTimeIntervalEqualToGskTimeInterval(gskDocument, metadata));
    }

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.OK);
  }

  @Test
  public void failedTestAllTimeIntervalEqualToGskTimeInterval() {
    prepareDataForTest(Arrays.asList("for-failed-test-time-sequence"));
    ValidationResult validationResult = ValidationResult.success();
    List<Metadata> metadataList = metadataDao.listWithEmptyValidField(clearDatabaseRule.getAwid()).getItemList();

    for (Metadata metadata : metadataList) {
      GskDocument gskDocument = gskDocumentDao.getByMetadataId(clearDatabaseRule.getAwid(), metadata.getId());
      validationResult.merge(documentValidationHelper.allTimeIntervalEqualToGskTimeInterval(gskDocument, metadata));
    }

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.ERROR);
    assertEquals(Error.TIME_INTERVAL_NO_VALID.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
    assertEquals(Error.TIME_INTERVAL_NO_VALID.getErrorCode().getErrorCode(), validationResult.getValidationMessages().get(0).getCode());
  }

  @Test
  public void testTimeSeriesIdIsSequential() {
    ValidationResult validationResult = getValidationResultOfTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"), "timeSeriesIdIsSequential");

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(ValidationResultSeverity.OK, validationResult.getSeverity());
  }

  @Test
  public void testFailedTimeSeriesIdIsSequential() {
    ValidationResult validationResult = getValidationResultOfTest(Collections.singletonList("for-failed-test-sequence"), "timeSeriesIdIsSequential");

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(ValidationResultSeverity.ERROR, validationResult.getSeverity());
    assertEquals(Error.TIME_SERIES_ID_IS_NOT_SEQUENCE.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
    assertEquals(Error.TIME_SERIES_ID_IS_NOT_SEQUENCE.getErrorCode().getErrorCode(), validationResult.getValidationMessages().get(0).getCode());
  }

  @Test
  public void testEachBlockContainNodes() {
    ValidationResult validationResult = getValidationResultOfTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"), "eachBlockContainNodes");

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(ValidationResultSeverity.OK, validationResult.getSeverity());
  }

  @Test
  public void testFailedEachBlockContainNodes() {
    ValidationResult validationResult = getValidationResultOfTest(Collections.singletonList("for-failed-test-contain-nodes"), "eachBlockContainNodes");

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(ValidationResultSeverity.ERROR, validationResult.getSeverity());
    assertEquals(Error.LESS_THEN_THREE_NODE.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
  }

  @Test
  public void testOneBlockListPresentInFile() {
    ValidationResult validationResult = getValidationResultOfTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"), "oneBlockListPresentInFile");

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.OK);
  }

  @Test
  public void failedTestOneBlockListPresentInFile() {
    ValidationResult validationResult = getValidationResultOfTest(Collections.singletonList("for-failed-test"), "oneBlockListPresentInFile");

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(Error.DOCUMENT_HAS_SEVERAL_TYPE_BLOCK.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
  }

  @Test
  public void testNodeNameAlwaysPresent() {
    ValidationResult validationResult = getValidationResultOfTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"), "nodeNameAlwaysPresent");

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.OK);
  }

  @Test
  public void failedTestNodeNameAlwaysPresent() {
    ValidationResult validationResult = getValidationResultOfTest(Collections.singletonList("for-failed-test"), "nodeNameAlwaysPresent");

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.ERROR);
    assertEquals(Error.AUTO_NODE_NAME_EMPTY.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
  }

  @Test
  public void testCheckAreaCodingName() {
    ValidationResult validationResult = getValidationResultOfTest(Arrays.asList("F103-GenerationAndLoadShiftKeys_BE_v02", "F103-GenerationAndLoadShiftKeys_CZ_v01"), "checkAreaCodingName");

    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.OK);
  }

  @Test
  public void failedTestCheckAreaCodingName() {
    ValidationResult validationResult = getValidationResultOfTest(Collections.singletonList("for-failed-test"), "checkAreaCodingName");

    assertFalse(validationResult.getValidationMessages().isEmpty());
    assertEquals(validationResult.getSeverity(), ValidationResultSeverity.ERROR);
    assertEquals(Error.AREA_EIC_NOT_VALID.getMessage(), validationResult.getValidationMessages().get(0).getDetail());
  }

  private ValidationResult getValidationResultOfTest(List<String> filenames, String methodName) {
    prepareDataForTest(filenames);
    ValidationResult validationResult = ValidationResult.success();
    List<Metadata> metadataList = metadataDao.listWithEmptyValidField(clearDatabaseRule.getAwid()).getItemList();

    for (Metadata metadata : metadataList) {
      GskDocument gskDocument = gskDocumentDao.getByMetadataId(clearDatabaseRule.getAwid(), metadata.getId());
      keyMap.get(methodName).accept(gskDocument, validationResult);
    }
    return validationResult;
  }

  private void prepareDataForTest(List<String> filenames) {
    for (String filename : filenames) {
      try (InputStream resourceAsStream = getClass().getResourceAsStream(filename + ".xml")) {
        GskDocumentParser documentParser = new GskDocumentParser();
        GskDocument gskDocument = documentParser.process(resourceAsStream);
        gskDocument.setAwid(clearDatabaseRule.getAwid());
        gskDocument.setGskSeries(documentParser.getSeriesList());

        Metadata metadata = documentParser.getMetadata();
        metadata.setAwid(clearDatabaseRule.getAwid());
        metadata.setFileName(filename + ".xml");

        Metadata savedMetadata = metadataDao.create(metadata);

        gskDocument.setMetadataId(savedMetadata.getId());
        gskDocumentDao.create(gskDocument);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  private void validationResultForOneBlockTest(ValidationResult validationResult, GskDocument g) {
    validationResult.merge(documentValidationHelper.oneBlockListPresentInFile(g));
  }

  private void validationResultForCheckAreaCodingName(ValidationResult validationResult, GskDocument g) {
    validationResult.merge(documentValidationHelper.checkAreaCodingName(g));
  }

  private void validationResultForNodeNameAlwaysPresent(ValidationResult validationResult, GskDocument g) {
    validationResult.merge(documentValidationHelper.nodeNameAlwaysPresent(g));
  }

  private void validationResultForEachBlockContainNodes(ValidationResult validationResult, GskDocument g) {
    validationResult.merge(documentValidationHelper.eachBlockContainNodes(g));
  }

  private void validationResultForTimeSeriesIdIsSequential(ValidationResult validationResult, GskDocument g) {
    validationResult.merge(documentValidationHelper.timeSeriesIdIsSequential(g));
  }

  @Configuration
  public static class DocumentValidationHelperConfiguration {

    @Bean
    DocumentValidationHelper documentValidationHelper() {
      return new DocumentValidationHelper();
    }

    @Bean
    ClearDatabaseRule clearDatabaseRule() {
      return new ClearDatabaseRule(Arrays.asList(metadataDao(), gskDocumentDao()));
    }

    @Bean
    MetadataDao metadataDao() {
      return new MetadataMongoDao();
    }

    @Bean
    GskDocumentDao gskDocumentDao() {
      return new GskDocumentMongoDao();
    }

  }
}
