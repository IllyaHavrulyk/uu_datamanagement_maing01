package uu.datamanagement.main.api.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.WriteResult;
import uu.app.dto.AbstractDtoOut;
import uu.app.exception.AppErrorMap;

public class ClearStorageDtoOut extends AbstractDtoOut {

  private AppErrorMap uuAppErrorMap;
  private WriteResult resultMetadata;
  private WriteResult resultGSKDocument;

  public AppErrorMap getUuAppErrorMap() {
    return uuAppErrorMap;
  }

  public void setUuAppErrorMap(AppErrorMap uuAppErrorMap) {
    this.uuAppErrorMap = uuAppErrorMap;
  }

  public WriteResult getResultMetadata() {
    return resultMetadata;
  }

  public void setResultMetadata(WriteResult resultMetadata) {
    this.resultMetadata = resultMetadata;
  }

  public WriteResult getResultGSKDocument() {
    return resultGSKDocument;
  }

  public void setResultGSKDocument(WriteResult resultGSKDocument) {
    this.resultGSKDocument = resultGSKDocument;
  }

  @Override
  public String toString() {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }

}
