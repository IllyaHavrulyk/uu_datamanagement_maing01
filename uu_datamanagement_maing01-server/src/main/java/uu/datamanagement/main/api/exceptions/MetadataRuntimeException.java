package uu.datamanagement.main.api.exceptions;

import java.util.Map;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class MetadataRuntimeException extends AppRuntimeException {

  private static final String ERROR_PREFIX = "uu-datamanagement-main/metadata/";
  private static final String INVALID_DTO_IN_ERROR_MESSAGE = "DtoIn is not valid.";

  public MetadataRuntimeException(MetadataRuntimeException.Error error, Map<String, ?> paramMap) {
    super(error.getErrorCode(), error.getMessage(), (AppErrorMap) null, paramMap, null);
  }

  public MetadataRuntimeException(Error error, Throwable cause) {
    super(error.getErrorCode(), error.getMessage(), cause, (Object) null);
  }

  public enum Error implements ErrorDefinition {

    UPDATE_INVALID_DTO_IN(ERROR_PREFIX + "invalidDtoIn", INVALID_DTO_IN_ERROR_MESSAGE),
    LIST_INVALID_DTO_IN(ERROR_PREFIX + "invalidDtoIn", INVALID_DTO_IN_ERROR_MESSAGE),
    UPDATE_METADATA_FAILED(ERROR_PREFIX + "updateMetadataFailed", "Failed update metadata."),
    GET_METADATA_FAILED(ERROR_PREFIX + "getById", "Metadata by dtoIn.id doesn't exist.");

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
