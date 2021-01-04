package uu.datamanagement.main.validation.exception;

import uu.app.exception.ErrorCode;

public interface ErrorDefinition {

  String getMessage();

  ErrorCode getCode();
}
