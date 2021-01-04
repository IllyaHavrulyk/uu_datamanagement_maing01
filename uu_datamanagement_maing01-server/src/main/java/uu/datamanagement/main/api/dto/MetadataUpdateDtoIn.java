package uu.datamanagement.main.api.dto;

import uu.app.validation.ValidationType;

@ValidationType("metadataUpdateDtoInType")
public class MetadataUpdateDtoIn {

  private String id;
  private String sender;
  private String receiver;
  private String domain;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
