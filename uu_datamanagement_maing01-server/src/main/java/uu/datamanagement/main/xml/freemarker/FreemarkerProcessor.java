package uu.datamanagement.main.xml.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class FreemarkerProcessor {

  private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

  private final Configuration freemarkerConfiguration;

  @Inject
  public FreemarkerProcessor(Configuration freemarkerConfiguration) {
    this.freemarkerConfiguration = freemarkerConfiguration;
  }

  public byte[] generateBinaryOutput(String templateName, Map<String, Object> input) throws TemplateException, IOException {
    byte[] bytes = null;
    Template template = freemarkerConfiguration.getTemplate(templateName);

    ByteArrayOutputStream allInMemoryStream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
    try (Writer writer = new OutputStreamWriter(allInMemoryStream, StandardCharsets.UTF_8)) {
      if (template != null) {
        template.process(input, writer);
        bytes = allInMemoryStream.toByteArray();
      }
    }
    return bytes;
  }

}
