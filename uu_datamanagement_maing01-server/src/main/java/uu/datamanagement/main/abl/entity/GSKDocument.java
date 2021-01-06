package uu.datamanagement.main.abl.entity;

import java.util.List;
import uu.app.objectstore.mongodb.domain.AbstractUuObject;

public class GSKDocument extends AbstractUuObject {

  private String documentIdentification;
  private String metadataId;
  private List<GSKSeries> gskSeries;

  public GSKDocument() {
  }

  public GSKDocument(String documentIdentification, String metadataId, List<GSKSeries> series) {
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

  public List<GSKSeries> getGskSeries() {
    return gskSeries;
  }

  public void setGskSeries(List<GSKSeries> gskSeries) {
    this.gskSeries = gskSeries;
  }
}
