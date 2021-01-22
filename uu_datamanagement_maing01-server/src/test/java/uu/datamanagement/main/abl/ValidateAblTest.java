package uu.datamanagement.main.abl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.junit.Before;
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
import uu.app.validation.Validator;
import uu.app.validation.spi.DefaultValidationResult;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.ValidateAblTest.ValidateAblTestConfiguration;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoIn;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.GskDocumentMongoDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.rules.ClearDatabaseRule;
import uu.datamanagement.main.serde.GskDocumentBuilder;
import uu.datamanagement.main.validation.DocumentValidationHelper;
import uu.datamanagement.main.validation.ValidationResult;
import uu.datamanagement.main.validation.ValidationResultSeverity;
import uu.datamanagement.main.xml.freemarker.FreemarkerProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
  SubAppPersistenceConfiguration.class,
  ValidateAblTestConfiguration.class
})
public class ValidateAblTest {

  @Rule
  @Autowired
  public ClearDatabaseRule clearDatabaseRule;

  @Inject
  private ValidateAbl validateAbl;

  @Inject
  private Validator validator;

  @Inject
  private GskDocumentAbl gskDocumentAbl;

  @Before
  public void setUp() {
    GskDocumentCreateDtoIn dtoIn = new GskDocumentCreateDtoIn();
    List<String> filenamesForTest = Arrays.asList("F103-GenerationAndLoadShiftKeys_CZ_v01", "F103-GenerationAndLoadShiftKeys_DE-50Hz_v01", "F103-GenerationAndLoadShiftKeys_HR_v01");

    for (String filename : filenamesForTest) {
      try (InputStream resourceAsStream = getClass().getResourceAsStream(filename + ".xml")) {
        MockMultipartFile multipartFile = new MockMultipartFile(filename, filename + ".xml", MediaType.MULTIPART_FORM_DATA.getType(), resourceAsStream);
        dtoIn.setDocument(multipartFile);
      } catch (IOException e) {
        e.printStackTrace();
      }

      when(validator.validate(dtoIn)).thenReturn(new DefaultValidationResult());
      gskDocumentAbl.create(clearDatabaseRule.getAwid(), dtoIn);
    }
  }

  @Test
  public void validateTest() {
    long startCmd = Instant.now().toEpochMilli();
    ValidationResult validationResult = validateAbl.validate(clearDatabaseRule.getAwid());
    long endCmd = Instant.now().toEpochMilli();

    assertTrue(validationResult.isValid());
    assertTrue(validationResult.getValidationMessages().isEmpty());
    assertEquals(ValidationResultSeverity.OK, validationResult.getSeverity());
    assertTrue(endCmd - startCmd > 3000);
  }

  @Configuration
  public static class ValidateAblTestConfiguration {

    @Bean
    ValidateAbl validateAbl() {
      return new ValidateAbl(documentValidationHelper(), gskDocumentDao(), metadataDao());
    }

    @Bean
    GskDocumentDao gskDocumentDao() {
      return new GskDocumentMongoDao();
    }

    @Bean
    MetadataDao metadataDao() {
      return new MetadataMongoDao();
    }

    @Bean
    DocumentValidationHelper documentValidationHelper() {
      return new DocumentValidationHelper();
    }

    @Bean
    GskDocumentAbl gskDocumentAbl() {
      return new GskDocumentAbl(gskDocumentDao(), metadataDao(), validationHelper(), modelMapper(), gskDocumentBuilder());
    }

    @Bean
    GskDocumentBuilder gskDocumentBuilder() {
      try {
        return new GskDocumentBuilder(freemarkerProcessor());
      } catch (TemplateException e) {
        e.printStackTrace();
      }
      return Mockito.mock(GskDocumentBuilder.class);
    }

    @Bean
    FreemarkerProcessor freemarkerProcessor() throws TemplateException {
      return new FreemarkerProcessor(freemarkerConfiguration());
    }

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() throws TemplateException {
      freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
      cfg.setLocale(Locale.US);
      cfg.setClassForTemplateLoading(this.getClass(), "/freemarker/");
      cfg.setSetting("number_format", "0.#########");
      return cfg;
    }

    @Bean
    Validator validator() {
      return Mockito.mock(Validator.class);
    }

    @Bean
    ModelMapper modelMapper() {
      return new ModelMapper();
    }

    @Bean
    ValidationHelper validationHelper() {
      return new ValidationHelper(validator());
    }

    @Bean
    ClearDatabaseRule clearDatabaseRule() {
      return new ClearDatabaseRule(Arrays.asList(metadataDao(), gskDocumentDao()));
    }

  }
}
