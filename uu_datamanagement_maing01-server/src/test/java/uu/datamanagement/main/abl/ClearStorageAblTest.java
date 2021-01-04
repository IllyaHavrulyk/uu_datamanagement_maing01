package uu.datamanagement.main.abl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.ClearStorageDtoIn;
import uu.datamanagement.main.api.dto.ClearStorageDtoOut;
import uu.datamanagement.main.dao.GSKDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.GSKDocumentMongoDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.rules.ClearDatabaseRule;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
  SubAppPersistenceConfiguration.class,
  ClearStorageAblTest.ClearStorageAblTestConfig.class
})
public class ClearStorageAblTest {

  @Autowired
  @Rule
  public ClearDatabaseRule clearDatabaseRule;

  @Inject
  private ClearStorageAbl clearStorageAbl;

  @Inject
  private MetadataDao metadataDao;

  @Inject
  private GSKDocumentDao gskDocumentDao;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    prepareTestData();
  }

  @Test
  public void clean() {

    ClearStorageDtoOut clear = clearStorageAbl.clean(clearDatabaseRule.getAwid(), new ClearStorageDtoIn());

    assertThat(clear.getResultGSKDocument().getN()).isEqualTo(3);
    assertThat(clear.getResultGSKDocument().getUpsertedId()).isNull();
    assertThat(clear.getResultMetadata().getN()).isEqualTo(3);
    assertThat(clear.getResultMetadata().getUpsertedId()).isNull();
  }

  private void prepareTestData() {
    for (int i = 0; i < 3; i++) {
      Metadata metadata = new Metadata();
      metadata.setAwid(clearDatabaseRule.getAwid());
      metadata.setDomain("test #" + i);
      metadata.setFileName("fileName test #" + i);

      metadata = metadataDao.create(metadata);

      GSKDocument gskDocument = new GSKDocument();
      gskDocument.setAwid(clearDatabaseRule.getAwid());
      gskDocument.setMetadataId(metadata.getId());
      gskDocument.setDocumentIdentification("10XAT-APG------Z-20190220-F103-v" + i);

      gskDocumentDao.create(gskDocument);
    }
  }

  @Configuration
  public static class ClearStorageAblTestConfig {

    @Bean
    ClearStorageAbl clearStorageAbl() {
      return new ClearStorageAbl(metadataDao(), gskDocumentDao(), validationHelper());
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
    GSKDocumentDao gskDocumentDao() {
      return new GSKDocumentMongoDao();
    }

    @Bean
    ValidationHelper validationHelper() {
      return Mockito.mock(ValidationHelper.class);
    }

  }
}
