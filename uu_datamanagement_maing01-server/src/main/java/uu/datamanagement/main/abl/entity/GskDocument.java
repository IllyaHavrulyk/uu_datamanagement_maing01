package uu.datamanagement.main.abl.entity;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.app.objectstore.mongodb.domain.AbstractUuObject;

public class GskDocument extends AbstractUuObject {

  private String documentIdentification;
  private String metadataId;
  private List<GskSeries> gskSeries;

  public GskDocument() {
  }

  public GskDocument(String documentIdentification, String metadataId, List<GskSeries> series) {
    this.documentIdentification = documentIdentification;
    this.metadataId = metadataId;
    this.gskSeries = series;
  }

  public String getDocumentIdentification() {
    return documentIdentification;
  }

  public void setDocumentIdentification(String documentIdentification) {
    this.documentIdentification = documentIdentification;
  }

  public String getMetadataId() {
    return metadataId;
  }

  public void setMetadataId(String metadataId) {
    this.metadataId = metadataId;
  }

  public List<GskSeries> getGskSeries() {
    return gskSeries;
  }

  public void setGskSeries(List<GskSeries> gskSeries) {
    this.gskSeries = gskSeries;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("documentIdentification", documentIdentification)
      .append("metadataId", metadataId)
      .append("gskSeries", gskSeries)
      .toString();
  }
}
