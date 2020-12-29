package uu.datamanagement.main.abl;

import javax.inject.Inject;
import org.springframework.stereotype.Component;
import uu.app.validation.ValidationResult;
import uu.app.validation.Validator;
import uu.app.validation.utils.ValidationResultUtils;
import uu.app.workspace.Profile;
import uu.app.workspace.dto.profile.SysSetProfileDtoIn;
import uu.datamanagement.main.dao.DatamanagementMainDao;
import uu.datamanagement.main.api.dto.DatamanagementMainInitDtoIn;
import uu.datamanagement.main.api.dto.DatamanagementMainInitDtoOut;
import uu.datamanagement.main.api.exceptions.DatamanagementMainInitRuntimeException;
import uu.datamanagement.main.api.exceptions.DatamanagementMainInitRuntimeException.Error;

@Component
public final class DatamanagementMainAbl {

  private static final String AUTHORITIES_CODE = "Authorities";

  @Inject
  private Validator validator;

  @Inject
  private Profile profile;

  @Inject
  private DatamanagementMainDao datamanagementMainDao;

  public DatamanagementMainInitDtoOut init(String awid, DatamanagementMainInitDtoIn dtoIn) {
    // HDS 1
    ValidationResult validationResult = validator.validate(dtoIn);
    if (!validationResult.isValid()) {
      // A1
      throw new DatamanagementMainInitRuntimeException(Error.INVALID_DTO_IN, ValidationResultUtils.validationResultToAppErrorMap(validationResult));
    }

    // HDS 2
    SysSetProfileDtoIn setProfileDtoIn = new SysSetProfileDtoIn();
    setProfileDtoIn.setCode(AUTHORITIES_CODE);
    setProfileDtoIn.setRoleUri(dtoIn.getAuthoritiesUri());
    profile.setProfile(awid, setProfileDtoIn);

    // HDS 3 - HDS N
    // TODO Implement according to application needs...

    // HDS N+1
    return new DatamanagementMainInitDtoOut();
  }

}
