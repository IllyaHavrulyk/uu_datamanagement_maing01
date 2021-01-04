package uu.datamanagement.main.validation.exception;

import java.util.Map;
import uu.app.exception.AppRuntimeException;

public class ValidationRuntimeException extends AppRuntimeException {

  public ValidationRuntimeException(ErrorDefinition error, Map<String, ?> paramMap) {
    super(error.getCode(), error.getMessage(), null, paramMap, null, (Object[]) null);
  }

}
