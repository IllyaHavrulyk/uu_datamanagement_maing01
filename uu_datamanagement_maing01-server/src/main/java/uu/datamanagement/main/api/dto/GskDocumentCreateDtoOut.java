package uu.datamanagement.main.api.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.app.dto.AbstractDtoOut;

public class GskDocumentCreateDtoOut extends AbstractDtoOut {

  private String id;
  private String documentIdentification;
  private String metadataId;

  public GskDocumentCreateDtoOut() {
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("id", id)
      .append("documentIdentification", documentIdentification)
      .append("metadataId", metadataId)
      .toString();
  }
}
