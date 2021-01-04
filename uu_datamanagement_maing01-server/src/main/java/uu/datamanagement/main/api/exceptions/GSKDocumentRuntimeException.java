package uu.datamanagement.main.api.exceptions;

import java.util.Map;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.validation.exception.ErrorDefinition;

public class GSKDocumentRuntimeException extends AppRuntimeException {

  public GSKDocumentRuntimeException(MetadataRuntimeException.Error code, Map<String, ?> paramMap) {
    super(code.getCode(), code.getMessage(), (AppErrorMap) null, paramMap, null);
  }

  public enum Error implements ErrorDefinition {

    INVALID_DTO_IN(ErrorCode.application("uu-datamanagement-main/gskDocument/invalidDtoIn"), "DtoIn is not valid.");

    private ErrorCode code;

    private String message;

    Error(ErrorCode code, String message) {
      this.code = code;
      this.message = message;
    }

    public ErrorCode getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

  }

}
