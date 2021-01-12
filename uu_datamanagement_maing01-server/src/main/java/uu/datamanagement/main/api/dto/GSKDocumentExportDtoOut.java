package uu.datamanagement.main.api.dto;

import java.util.List;
import uu.app.dto.AbstractDtoOut;

public class GSKDocumentExportDtoOut extends AbstractDtoOut {

  private List<Byte> byteList;

  public GSKDocumentExportDtoOut() {
  }

  public List<Byte> getByteList() {
    return byteList;
  }

  public void setByteList(List<Byte> byteList) {
    this.byteList = byteList;
  }
}
