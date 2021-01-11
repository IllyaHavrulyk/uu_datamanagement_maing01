package uu.datamanagement.main.validation.exception;

import java.util.Map;
import uu.app.exception.AppRuntimeException;
import uu.app.exception.ErrorCode;
import uu.app.validation.ValidationError;

public class ValidationRuntimeException extends AppRuntimeException {

  public ValidationRuntimeException(ValidationError error, Map<String, ?> paramMap) {
    super(ErrorCode.system(error.getCode()), error.getMessage(), null, paramMap, null, (Object[]) null);
  }

}
