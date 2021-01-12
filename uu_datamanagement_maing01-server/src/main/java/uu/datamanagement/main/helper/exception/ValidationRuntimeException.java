package uu.datamanagement.main.helper.exception;

import java.util.Map;
import uu.app.exception.AppRuntimeException;

public class ValidationRuntimeException extends AppRuntimeException {

  public ValidationRuntimeException(ErrorDefinition error, Map<String, ?> paramMap) {
    super(error.getErrorCode(), error.getMessage(), null, paramMap, null, (Object[]) null);
  }

}
