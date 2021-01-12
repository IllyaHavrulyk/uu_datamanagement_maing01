package uu.datamanagement.main.api.dto;

import java.util.List;
import uu.app.dto.AbstractDtoOut;

public class GskDocumentExportDtoOut extends AbstractDtoOut {

  private List<Byte> byteList;

  public GskDocumentExportDtoOut() {
  }

  public List<Byte> getByteList() {
    return byteList;
  }

  public void setByteList(List<Byte> byteList) {
    this.byteList = byteList;
  }
}
