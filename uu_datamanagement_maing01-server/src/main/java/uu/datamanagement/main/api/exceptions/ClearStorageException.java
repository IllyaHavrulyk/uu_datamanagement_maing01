package uu.datamanagement.main.api.exceptions;

import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class ClearStorageException extends AppRuntimeException {


  public ClearStorageException(Error error, Throwable cause) {
    super(error.getErrorCode(), error.getMessage(), cause, (Object) null);
  }

  public enum Error implements ErrorDefinition {

    INVALID_DTO_IN("uu-datamanagement-main/gskdocument/clean/invalidDtoIn", "DtoIn is not valid.");

    private final String code;
    private final String message;

    Error(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public ErrorCode getErrorCode() {
      return ErrorCode.application(code);
    }
  }
}
