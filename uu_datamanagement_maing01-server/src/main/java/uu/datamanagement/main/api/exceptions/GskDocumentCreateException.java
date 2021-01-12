package uu.datamanagement.main.api.exceptions;

import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class GskDocumentCreateException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/gskdocument/create/";

  public GskDocumentCreateException(Error error, Throwable cause) {
    super(error.getErrorCode(), error.getMessage(), cause, (Object) null);
  }

  public enum Error implements ErrorDefinition {

    INVALID_DTO_IN(ERROR_PREFIX + "invalidDtoIn", "DtoIn is not valid."),
    DESERELIZATION_FAILED(ERROR_PREFIX + "deserealizationFailed", "Failed to parse document."),
    CREATE_METADATA_FAILED(ERROR_PREFIX + "createMetadataFailed", "Failed to create metadata."),
    CREATE_GSK_DOCUMENT_FAILED(ERROR_PREFIX + "createGskDocumentFailed", "Failed to create gskDocument.");

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
      return ErrorCode.system(code);
    }
  }
}
