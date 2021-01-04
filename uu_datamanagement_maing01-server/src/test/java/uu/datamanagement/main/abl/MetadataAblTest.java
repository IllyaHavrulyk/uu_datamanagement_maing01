package uu.datamanagement.main.abl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static uu.datamanagement.main.api.exceptions.MetadataRuntimeException.Error.INVALID_DTO_IN;

import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Ignore;
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
import uu.app.testSupport.AppRuntimeExceptionMatcher;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.MetadataDtoOut;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoIn;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.rules.ClearDatabaseRule;
import uu.datamanagement.main.validation.exception.ValidationRuntimeException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
  SubAppPersistenceConfiguration.class,
  MetadataAblTest.MetadataAblTestConfiguration.class
})
public class MetadataAblTest {

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
        hasProperty("domain", is("test #0")),
        hasProperty("fileName", is("fileName test #0"))
      ),
      allOf(
        hasProperty("awid", is(clearDatabaseRule.getAwid())),
        hasProperty("domain", is("test #1")),
        hasProperty("fileName", is("fileName test #1"))
      ),
      allOf(
        hasProperty("awid", is(clearDatabaseRule.getAwid())),
        hasProperty("domain", is("test #2")),
        hasProperty("fileName", is("fileName test #2"))
      )
    ));
  }

  private void prepareTestData(int count) {
    for (int i = 0; i < count; i++) {
      Metadata metadata = new Metadata();
      metadata.setAwid(clearDatabaseRule.getAwid());
      metadata.setDomain("test #" + i);
      metadata.setFileName("fileName test #" + i);

      metadataDao.create(metadata);
    }
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
      return Mockito.mock(ModelMapper.class);
    }
  }
}
