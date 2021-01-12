package uu.datamanagement.main.helper.exception;

import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;

public class GskDocumentParserException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/helper/parser/abstractXmlParser";

  public GskDocumentParserException(GskDocumentParserException.Error code, String message) {
    super(code.getErrorCode(), message, (AppErrorMap) null);
  }

  public enum Error implements ErrorDefinition {

    DESERIALIZATION_FAILED(ERROR_PREFIX + "process/deserializationFailed", "Deserialization failed.");

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
