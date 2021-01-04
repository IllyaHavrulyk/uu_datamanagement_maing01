package uu.datamanagement.main.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.ZonedDateTime;

public class TimeIntervalDeserializer extends StdDeserializer<TimeInterval> {

  public TimeIntervalDeserializer(Class<?> vc) {
    super(vc);
  }

  public TimeIntervalDeserializer() {
    this(null);
  }

  @Override
  public TimeInterval deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    ZonedDateTime from = null;
    ZonedDateTime to = null;

    if (nodeIsPresent(node)) {
      if (nodeIsPresent(node.get("from"))) {
        from = ZonedDateTime.parse(node.get("from").asText());
      }
      if (nodeIsPresent(node.get("to"))) {
        to = ZonedDateTime.parse(node.get("to").asText());
      }
      if (from != null && to != null) {
        return new TimeInterval(from, to);
      }
    }
    return null;
  }

  private boolean nodeIsPresent(JsonNode node) {
    return node != null && !node.isNull();
  }

}
