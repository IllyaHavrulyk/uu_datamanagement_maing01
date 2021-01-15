package uu.datamanagement.main.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GskDocument;

@Component
public class AssertArchiveSupport {

  public void assertArchive(byte[] bytes, Map<String, Consumer<GskDocument>> assertMap) {

  }

  public void main(byte[] bytes) {
    HashMap<String, Consumer<GskDocument>> rules = new HashMap<>();
    rules.put("gsk-create-hds.xml", this::checkValues);
  }

  private void checkValues(GskDocument gskDocument) {
    assert !gskDocument.getGskSeries().isEmpty();
    assert gskDocument.getGskSeries().size() == 1;
    assert !gskDocument.getGskSeries().get(0).getManualGskBlocks().isEmpty();
    assert !gskDocument.getGskSeries().get(0).getManualGskBlocks().get(0).getManualNodes().isEmpty();
    assert gskDocument.getGskSeries().get(0).getManualGskBlocks().get(0).getManualNodes().size() == 3;
  }

}
