package uu.datamanagement.main.abl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.ZonedDateTime;
import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.MetadataDtoOut;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoIn;
import uu.datamanagement.main.api.exceptions.MetadataRuntimeException;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.rules.ClearDatabaseRule;
import uu.datamanagement.main.utils.DocumentType;
import uu.datamanagement.main.utils.TimeInterval;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
  SubAppPersistenceConfiguration.class,
  MetadataAblTest.MetadataAblTestConfiguration.class
})
public class MetadataAblTest {

  private final TimeInterval timeInterval = new TimeInterval(ZonedDateTime.parse("2019-02-19T23:00Z"), ZonedDateTime.parse("2019-02-20T23:00Z"));

  @Autowired
  @Rule
  public ClearDatabaseRule clearDatabaseRule;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Inject
  MetadataDao metadataDao;

  @Inject
  MetadataAbl metadataAbl;

  @Test
  public void testListMetadata() {
    prepareTestData(3);

    MetadataListDtoOut dtoOut = metadataAbl.list(clearDatabaseRule.getAwid(), new MetadataListDtoIn());

    Assert.assertNotNull(dtoOut);
    Assert.assertEquals(3, dtoOut.getItemList().size());
    assertThat(dtoOut.getItemList(), containsInAnyOrder(
      allOf(
        hasProperty("awid", is(clearDatabaseRule.getAwid())),
        hasProperty("domain", is("00Y1001C--00059P")),
        hasProperty("fileName", is("fileName test #0"))
      ),
      allOf(
        hasProperty("awid", is(clearDatabaseRule.getAwid())),
        hasProperty("domain", is("10Y1001C--00059P")),
        hasProperty("fileName", is("fileName test #1"))
      ),
      allOf(
        hasProperty("awid", is(clearDatabaseRule.getAwid())),
        hasProperty("domain", is("20Y1001C--00059P")),
        hasProperty("fileName", is("fileName test #2"))
      )
    ));
  }

  @Test
  public void testUpdateMetadata() {
    String id = prepareTestData(1);

    MetadataUpdateDtoIn dtoIn = new MetadataUpdateDtoIn();
    dtoIn.setId(id);
    dtoIn.setSender("UPDATE-SENDER-EIC");
    dtoIn.setReceiver("UPDATE-RECEIVER-EIC");

    MetadataDtoOut dtoOut = metadataAbl.update(clearDatabaseRule.getAwid(), dtoIn);

    assertEquals(DocumentType.B22, dtoOut.getDocumentType());
    assertEquals("UPDATE-SENDER-EIC", dtoOut.getSender());
    assertEquals("UPDATE-RECEIVER-EIC", dtoOut.getReceiver());
    assertEquals(timeInterval, dtoOut.getTimeInterval());
    assertEquals(ZonedDateTime.parse("2019-02-13T09:30:00Z"), dtoOut.getCreationDateTime());
    assertEquals("00Y1001C--00059P", dtoOut.getDomain());
    assertEquals("fileName test #0", dtoOut.getFileName());
  }

  @Test(expected = MetadataRuntimeException.class)
  public void failedTestUpdateMetadata() {
    MetadataUpdateDtoIn dtoIn = new MetadataUpdateDtoIn();
    dtoIn.setId("FAILED-ID");
    dtoIn.setSender("UPDATE-SENDER-EIC");
    dtoIn.setReceiver("UPDATE-RECEIVER-EIC");

    metadataAbl.update(clearDatabaseRule.getAwid(), dtoIn);
  }

  private String prepareTestData(int count) {
    String lastMetadataId = "";
    for (int i = 0; i < count; i++) {
      Metadata metadata = new Metadata();
      metadata.setAwid(clearDatabaseRule.getAwid());
      metadata.setDocumentType(DocumentType.B22);
      metadata.setSender("SENDER-EIC-" + i);
      metadata.setReceiver("RECEIVER-EIC-" + i);
      metadata.setCreationDateTime(ZonedDateTime.parse("2019-02-13T09:30:00Z"));
      metadata.setTimeInterval(timeInterval);
      metadata.setDomain(i + "0Y1001C--00059P");
      metadata.setFileName("fileName test #" + i);

      lastMetadataId = metadataDao.create(metadata).getId();
    }
    return lastMetadataId;
  }

  @Configuration
  public static class MetadataAblTestConfiguration {

    @Bean
    public ClearDatabaseRule clearDatabaseRule() {
      return new ClearDatabaseRule(Arrays.asList(metadataDao()));
    }

    @Bean
    MetadataAbl metadataAbl() {
      return new MetadataAbl(metadataDao(), validationHelper(), modelMapper());
    }

    @Bean
    ValidationHelper validationHelper() {
      return Mockito.mock(ValidationHelper.class);
    }

    @Bean
    MetadataDao metadataDao() {
      return new MetadataMongoDao();
    }

    @Bean
    ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }
}
