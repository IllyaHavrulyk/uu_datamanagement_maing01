package uu.datamanagement.main.abl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.inject.Inject;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.api.dto.GSKDocumentDtoIn;
import uu.datamanagement.main.api.dto.GSKDocumentDtoOut;
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
  GSKDocumentAblTest.GSKDocumentAblTestConfiguration.class
})
public class GSKDocumentAblTest {

  @Autowired
  @Rule
  public ClearDatabaseRule clearDatabaseRule;

  @Inject
  private GSKDocumentAbl gskDocumentAbl;

  @Test
  public void testCreateGSKDocument() {
    GSKDocumentDtoIn dtoIn = generateGSKDocumentDtoIn();

    GSKDocumentDtoOut dtoOut = gskDocumentAbl.create(clearDatabaseRule.getAwid(), dtoIn);

    assertNotNull(dtoOut.getMetadataId());
    assertEquals("10XAT-APG------Z-20190220-F103-v1", dtoOut.getDocumentIdentification());
    assertEquals(1, dtoOut.getGskSeries().size());
    assertEquals("10YAT-APG------L", dtoOut.getGskSeries().get(0).getArea());
    assertEquals(3, dtoOut.getGskSeries().get(0).getLastManualBlock().getManualNodes().size());
  }

  private GSKDocumentDtoIn generateGSKDocumentDtoIn() {
    GSKDocumentDtoIn dtoIn = new GSKDocumentDtoIn();
    dtoIn.setName("F103-GeneratingAndLoadShiftKeys");
    dtoIn.setText("File for testing request");

    try (InputStream resourceAsStream = getClass().getResourceAsStream("gsk-create-hds.xml")) {
      MockMultipartFile multipartFile = new MockMultipartFile("gsk-create-hds", "gsk-create-hds.xml", MediaType.MULTIPART_FORM_DATA.getType(), resourceAsStream);
      dtoIn.setDocument(multipartFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return dtoIn;
  }

  @Configuration
  public static class GSKDocumentAblTestConfiguration {

    @Bean
    GSKDocumentAbl gskDocumentAbl() {
      return new GSKDocumentAbl(gskDocumentDao(), metadataDao(), validationHelper(), modelMapper());
    }

    @Bean
    GSKDocumentDao gskDocumentDao() {
      return new GSKDocumentMongoDao();
    }

    @Bean
    MetadataDao metadataDao() {
      return new MetadataMongoDao();
    }

    @Bean
    ValidationHelper validationHelper() {
      return Mockito.mock(ValidationHelper.class);
    }

    @Bean
    ModelMapper modelMapper() {
      return new ModelMapper();
    }

    @Bean
    ClearDatabaseRule clearDatabaseRule() {
      return new ClearDatabaseRule(Arrays.asList(metadataDao(), gskDocumentDao()));
    }

  }
}
