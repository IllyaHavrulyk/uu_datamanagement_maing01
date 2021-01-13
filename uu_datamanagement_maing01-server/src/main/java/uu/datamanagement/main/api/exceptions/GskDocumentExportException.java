package uu.datamanagement.main.api.exceptions;

import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class GskDocumentExportException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/gskdocument/export/";

  public GskDocumentExportException(Error error, Throwable cause) {
    super(error.getErrorCode(), error.getMessage(), cause, (Object) null);
  }

  public GskDocumentExportException(Error error, Object... params) {
    super(error.getErrorCode(), error.getMessage(), params);
  }

  public enum Error implements ErrorDefinition {

    INVALID_DTO_IN(ERROR_PREFIX + "invalidDtoIn", "DtoIn is not valid."),
    GET_METADATA_FAILED(ERROR_PREFIX + "generateZipArchive/metadataGetByIdFailed", "Metadata by dtoIn.id doesn't exist."),
    BUILD_DOCUMENT_FAILED(ERROR_PREFIX + "generateZipArchive/buildDocumentFailed", "GskDocumentBuilder can't execute cmd build."),
    GET_GENERATED_ZIP_FAILED(ERROR_PREFIX + "getGeneratedZipFailed", "Get bytes from generated zip archive failed.");

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
