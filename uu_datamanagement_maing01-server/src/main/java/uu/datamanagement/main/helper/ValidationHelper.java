package uu.datamanagement.main.helper;

import javax.inject.Inject;
import org.springframework.stereotype.Component;
import uu.app.validation.ValidationError;
import uu.app.validation.ValidationResult;
import uu.app.validation.Validator;
import uu.app.validation.utils.ValidationResultUtils;
import uu.datamanagement.main.validation.exception.ErrorDefinition;
import uu.datamanagement.main.validation.exception.ValidationRuntimeException;

@Component
public class ValidationHelper {

  private final Validator validator;

  @Inject
  public ValidationHelper(Validator validator) {
    this.validator = validator;
  }

  /**
   * Validates the value using validation type defined on the value object class.
   *
   * @param dtoIn object to be validated by the scheme defined on it
   * @param error error to be thrown if the object value is not valid
   * @throws ValidationRuntimeException with the given error if the object value is not valid.
   */
  public void validateDtoIn(Object dtoIn, ValidationError error) {
    ValidationResult validationResult = validator.validate(dtoIn);
    checkValidationResult(error, validationResult);
  }

  private void checkValidationResult(ValidationError error, ValidationResult validationResult) {
    if (!validationResult.isValid()) {
      throw new ValidationRuntimeException(error, ValidationResultUtils.validationResultToAppErrorMap(validationResult));
    }
  }
}
