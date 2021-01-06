package uu.datamanagement.main.api.exceptions;

import java.util.Map;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.datamanagement.main.validation.exception.ErrorDefinition;

public final class DatamanagementMainInitRuntimeException extends AppRuntimeException {

  public DatamanagementMainInitRuntimeException(DatamanagementMainInitRuntimeException.Error code, Map<String, ?> paramMap) {
    super(code.getErrorCode(), code.getMessage(), (AppErrorMap) null, paramMap, null);
  }

  public enum Error implements ErrorDefinition {

    INVALID_DTO_IN(ErrorCode.application("uu-datamanagement-main/invalidDtoIn"), "DtoIn is not valid."),

    SET_PROFILE_FAILED(ErrorCode.application("uu-datamanagement-main/init/sys/setProfileFailed"), "Set profile failed.");

    private ErrorCode code;

    private String message;

    Error(ErrorCode code, String message) {
      this.code = code;
      this.message = message;
    }

    public ErrorCode getErrorCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

  }

}
