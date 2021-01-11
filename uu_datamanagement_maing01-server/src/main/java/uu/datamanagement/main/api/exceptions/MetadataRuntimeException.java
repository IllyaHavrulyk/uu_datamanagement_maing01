package uu.datamanagement.main.api.exceptions;

import java.util.Map;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.app.validation.ValidationError;
import uu.app.validation.ValidationErrorSeverity;
import uu.datamanagement.main.validation.exception.ErrorDefinition;

public class MetadataRuntimeException extends AppRuntimeException {

  public MetadataRuntimeException(MetadataRuntimeException.Error code, Map<String, ?> paramMap) {
    super(ErrorCode.system(code.getCode()), code.getMessage(), (AppErrorMap) null, paramMap, null);
  }

  public enum Error implements ValidationError {

    INVALID_DTO_IN(ErrorCode.application("uu-datamanagement-main/metadata/invalidDtoIn"), "DtoIn is not valid."),
    GET_METADATA_FAILED(ErrorCode.application("uu-datamanagement-main/metadata/getById"), "Metadata by dtoIn.id doesn't exist.");

    private final ErrorCode code;

    private final String message;

    Error(ErrorCode code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code.getErrorCode();
    }

    public String getMessage() {
      return message;
    }

    @Override
    public ValidationErrorSeverity getSeverity() {
      return ValidationErrorSeverity.ERROR;
    }

  }

}
