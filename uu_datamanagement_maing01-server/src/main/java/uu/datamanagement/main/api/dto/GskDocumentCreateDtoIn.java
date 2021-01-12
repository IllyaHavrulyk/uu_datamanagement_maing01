package uu.datamanagement.main.api.dto;

import org.springframework.web.multipart.MultipartFile;
import uu.app.validation.ValidationType;

@ValidationType("gskDocumentDtoInType")
public class GskDocumentCreateDtoIn {

  private String name;
  private String text;
  private MultipartFile document;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public MultipartFile getDocument() {
    return document;
  }

  public void setDocument(MultipartFile document) {
    this.document = document;
  }
}
