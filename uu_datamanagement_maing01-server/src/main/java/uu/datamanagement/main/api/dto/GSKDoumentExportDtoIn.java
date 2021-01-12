package uu.datamanagement.main.api.dto;

import uu.app.datastore.domain.PageInfo;
import uu.app.validation.ValidationType;

@ValidationType("gskDoumentExportDtoInType")
public class GSKDoumentExportDtoIn {

  private PageInfo pageInfo;

  public GSKDoumentExportDtoIn() {
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }
}
