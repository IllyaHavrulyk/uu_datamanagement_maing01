package uu.datamanagement.main.abl.entity;

import uu.app.objectstore.mongodb.domain.AbstractUuObject;

public class GSKDocument extends AbstractUuObject {

  private String documentIdentification;
  private String metadataId;
  private GSKSeries series;

  public GSKDocument() {
  }

  public GSKDocument(String documentIdentification, String metadataId, GSKSeries series) {
    this.documentIdentification = documentIdentification;
    this.metadataId = metadataId;
    this.series = series;
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

  public GSKSeries getSeries() {
    return series;
  }

  public void setSeries(GSKSeries series) {
    this.series = series;
  }
}
