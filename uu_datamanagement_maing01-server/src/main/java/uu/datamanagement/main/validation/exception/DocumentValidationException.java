package uu.datamanagement.main.validation.exception;

import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class DocumentValidationException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/validation/documentValidationHelper/";

  public DocumentValidationException(Error error, Object... params) {
    super(error.getErrorCode(), error.getMessage(), params);
  }

  public enum Error implements ErrorDefinition {
    TIME_INTERVAL_NO_VALID(ERROR_PREFIX + "allTimeIntervalEqualToGskTimeIntervalFailed", "SubTimeInterval not valid to main timeInterval."),
    AUTO_NODE_NAME_EMPTY(ERROR_PREFIX + "nodeNameAlwaysPresentFailed", "AutoBlock.NodeName is empty."),
    AREA_EIC_NOT_VALID(ERROR_PREFIX + "checkAreaCodingNameFailed", "AreaEic contain not only A-Z 0-9 - symbols."),
    DOCUMENT_HAS_SEVERAL_TYPE_BLOCK(ERROR_PREFIX + "oneBlockListPresentInFileFailed", "Document has block more than one type.");

    private final String message;
    private final String code;

    Error(String code, String message) {
      this.message = message;
      this.code = code;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public ErrorCode getErrorCode() {
      return ErrorCode.system(code);
    }
  }
}
