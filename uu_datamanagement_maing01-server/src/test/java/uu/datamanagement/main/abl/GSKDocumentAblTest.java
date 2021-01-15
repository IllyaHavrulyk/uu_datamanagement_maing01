package uu.datamanagement.main.abl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uu.app.server.dto.DownloadableResourceDtoOut;
import uu.app.validation.ValidationErrorType;
import uu.app.validation.Validator;
import uu.app.validation.spi.DefaultValidationError;
import uu.app.validation.spi.DefaultValidationResult;
import uu.datamanagement.main.SubAppPersistenceConfiguration;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoIn;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoOut;
import uu.datamanagement.main.api.dto.GskDoumentExportDtoIn;
import uu.datamanagement.main.api.exceptions.GskDocumentCreateException.Error;
import uu.datamanagement.main.dao.GskDocumentDao;
import uu.datamanagement.main.dao.MetadataDao;
import uu.datamanagement.main.dao.mongo.GskDocumentMongoDao;
import uu.datamanagement.main.dao.mongo.MetadataMongoDao;
import uu.datamanagement.main.helper.ValidationHelper;
import uu.datamanagement.main.helper.exception.ValidationRuntimeException;
import uu.datamanagement.main.helper.parser.GskDocumentParser;
import uu.datamanagement.main.rules.ClearDatabaseRule;
import uu.datamanagement.main.serde.GskDocumentBuilder;
import uu.datamanagement.main.xml.freemarker.FreemarkerProcessor;

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

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Inject
  private GskDocumentAbl gskDocumentAbl;

  @Inject
  private Validator validator;

  @Test
  public void testCreateGSKDocument() {
    GskDocumentCreateDtoIn dtoIn = generateGSKDocumentDtoIn();

    when(validator.validate(dtoIn)).thenReturn(new DefaultValidationResult());
    GskDocumentCreateDtoOut dtoOut = gskDocumentAbl.create(clearDatabaseRule.getAwid(), dtoIn);

    assertNotNull(dtoOut.getId());
    assertNotNull(dtoOut.getMetadataId());
    assertEquals("10XAT-APG------Z-20190220-F103-v1", dtoOut.getDocumentIdentification());
  }

  @Test(expected = ValidationRuntimeException.class)
  public void invalidGSKDocumentDtoIn() {
    GskDocumentCreateDtoIn dtoIn = new GskDocumentCreateDtoIn();

    DefaultValidationResult validationResult = new DefaultValidationResult();
    validationResult.addError(ValidationErrorType.MISSING_KEY, new DefaultValidationError(Error.INVALID_DTO_IN.getErrorCode().getErrorCode(), Error.INVALID_DTO_IN.getMessage()));

    when(validator.validate(dtoIn)).thenReturn((validationResult));

    gskDocumentAbl.create(clearDatabaseRule.getAwid(), dtoIn);
  }

  @Test
  public void testExportGSKDocumentsToZipArchive() {
    createdDataBeforeExportArchive();

    GskDoumentExportDtoIn dtoIn = new GskDoumentExportDtoIn();

    when(validator.validate(dtoIn)).thenReturn(new DefaultValidationResult());
    DownloadableResourceDtoOut dtoOut = gskDocumentAbl.export(clearDatabaseRule.getAwid(), dtoIn);

    List<GskDocument> gskDocuments = new ArrayList<>();
    List<Metadata> metadataList = new ArrayList<>();

    try (ZipInputStream zis = new ZipInputStream(dtoOut.getResource().getInputStream())) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(zis, baos);

        GskDocumentParser parser = new GskDocumentParser();

        GskDocument documentParser = parser.process(new ByteArrayInputStream(baos.toByteArray()));
        documentParser.setGskSeries(parser.getSeriesList());
        gskDocuments.add(documentParser);
        metadataList.add(parser.getMetadata());
        zis.closeEntry();
        baos.close();
        zipEntry = zis.getNextEntry();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    assertNotNull(dtoOut);
  }

  private void createdDataBeforeExportArchive() {
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

  private GskDocumentCreateDtoIn generateGSKDocumentDtoIn() {
    GskDocumentCreateDtoIn dtoIn = new GskDocumentCreateDtoIn();
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
    GskDocumentAbl gskDocumentAbl() {
      return new GskDocumentAbl(gskDocumentDao(), metadataDao(), validationHelper(), modelMapper(), gskDocumentBuilder());
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
    ValidationHelper validationHelper() {
      return new ValidationHelper(validator());
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
    ClearDatabaseRule clearDatabaseRule() {
      return new ClearDatabaseRule(Arrays.asList(metadataDao(), gskDocumentDao()));
    }

    @Bean
    GskDocumentBuilder gskDocumentBuilder() {
      try {
        return new GskDocumentBuilder(new FreemarkerProcessor(freemarkerConfiguration()));
      } catch (TemplateException e) {
        e.printStackTrace();
      }
      return Mockito.mock(GskDocumentBuilder.class);
    }

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() throws TemplateException {
      freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
      cfg.setLocale(Locale.US);
      cfg.setClassForTemplateLoading(this.getClass(), "/freemarker/");
      cfg.setSetting("number_format", "0.#########");
      return cfg;
    }


  }
}
