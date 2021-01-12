package uu.datamanagement.main.api.dto;

import uu.app.datastore.domain.PageInfo;
import uu.app.validation.ValidationType;

@ValidationType("gskDoumentExportDtoInType")
public class GskDoumentExportDtoIn {

  private PageInfo pageInfo;

  public GskDoumentExportDtoIn() {
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }
}
