package uu.datamanagement.main.api.exceptions;

import java.util.Map;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.app.validation.ValidationError;
import uu.app.validation.ValidationErrorSeverity;
import uu.datamanagement.main.validation.exception.ErrorDefinition;

public final class DatamanagementMainInitRuntimeException extends AppRuntimeException {

  public DatamanagementMainInitRuntimeException(DatamanagementMainInitRuntimeException.Error code, Map<String, ?> paramMap) {
    super(ErrorCode.system(code.getCode()), code.getMessage(), (AppErrorMap) null, paramMap, null);
  }

  public enum Error implements ValidationError {

    INVALID_DTO_IN(ErrorCode.application("uu-datamanagement-main/invalidDtoIn"), "DtoIn is not valid."),

    SET_PROFILE_FAILED(ErrorCode.application("uu-datamanagement-main/init/sys/setProfileFailed"), "Set profile failed.");

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
