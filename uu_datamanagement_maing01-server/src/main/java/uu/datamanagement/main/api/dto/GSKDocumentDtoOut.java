package uu.datamanagement.main.api.dto;

import java.util.List;
import uu.app.dto.AbstractDtoOut;
import uu.datamanagement.main.abl.entity.GSKSeries;

public class GSKDocumentDtoOut extends AbstractDtoOut {

  private String id;
  private String documentIdentification;
  private String metadataId;
  private List<GSKSeries> gskSeries;

  public GSKDocumentDtoOut() {
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
