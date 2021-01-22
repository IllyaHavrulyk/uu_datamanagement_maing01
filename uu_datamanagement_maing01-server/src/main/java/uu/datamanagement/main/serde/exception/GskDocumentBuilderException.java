package uu.datamanagement.main.serde.exception;

import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class GskDocumentBuilderException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/serde/gskDocumentBuilder";

  public GskDocumentBuilderException(Error error) {
    super(error.getErrorCode(), error.getMessage(), (Object) null);
  }

  public enum Error implements ErrorDefinition {

    BUILD_DOCUMENT_FAILED(ERROR_PREFIX + "build", "Deserialization failed.");

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

    public ErrorCode getErrorCode() {
      return ErrorCode.system(code);
    }


  }
}
