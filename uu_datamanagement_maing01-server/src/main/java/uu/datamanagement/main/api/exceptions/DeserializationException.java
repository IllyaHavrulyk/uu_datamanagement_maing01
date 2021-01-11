package uu.datamanagement.main.api.exceptions;

import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.validation.exception.ErrorDefinition;

public class DeserializationException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "usy-fbcore-light/parse/";

  public DeserializationException(DeserializationException.Error code, String message) {
    super(code.getErrorCode(), message, (AppErrorMap) null);
  }

  public enum Error implements ErrorDefinition {

    DESERIALIZATION_FAILED(ErrorCode.application(ERROR_PREFIX + "deserializationFailed"), "Deserialization failed.");

    private final ErrorCode code;

    private final String message;

    Error(ErrorCode code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    public ErrorCode getErrorCode() {
      return code;
    }


  }

}
