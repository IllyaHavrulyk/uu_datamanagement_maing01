package uu.datamanagement.main.api.dto;

import uu.app.dto.AbstractDtoOut;

public class GSKDocumentExportDtoOut extends AbstractDtoOut {

  private byte[] bytes;

  public GSKDocumentExportDtoOut() {
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }
}
