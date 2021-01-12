package uu.datamanagement.main.serde;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.xml.freemarker.FreemarkerProcessor;

@Component
public class GSKDocumentBuilder {

  private static final String GSK_TEMPLATE = "/gsk-document.ftl";

  private final FreemarkerProcessor freemarkerProcessor;

  @Inject
  public GSKDocumentBuilder(FreemarkerProcessor freemarkerProcessor) {
    this.freemarkerProcessor = freemarkerProcessor;
  }

  public byte[] build(GSKDocument gskDocument, Metadata metadata) {
    Map<String, Object> input = new HashMap<>();
    input.put("gskDocument", gskDocument);
    input.put("metadata", metadata);

    try {
      return freemarkerProcessor.generateBinaryOutput(GSK_TEMPLATE, input);
    } catch (TemplateException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
