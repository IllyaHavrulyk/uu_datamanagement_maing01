package uu.datamanagement.main.api.dto;

import uu.app.datastore.domain.PageInfo;
import uu.app.validation.ValidationType;

@ValidationType("metadataListDtoInType")
public class MetadataListDtoIn {

  private PageInfo pageInfo;

  public MetadataListDtoIn() {
  }

  public MetadataListDtoIn(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }
}
