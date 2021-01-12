package uu.datamanagement.main.helper.exception;

import uu.app.exception.ErrorCode;

public interface ErrorDefinition {

  String getMessage();

  ErrorCode getErrorCode();

}
