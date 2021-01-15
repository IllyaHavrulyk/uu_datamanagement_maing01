package uu.datamanagement.main.api;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.RequestMethod;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.ValidateAbl;
import uu.datamanagement.main.api.dto.GskDoumentExportDtoIn;
import uu.datamanagement.main.validation.ValidationResult;

@CommandController
public class ValidateController {

  private ValidateAbl validateAbl;

  @Inject
  public ValidateController(ValidateAbl validateAbl) {
    this.validateAbl = validateAbl;
  }

  @Command(path = "validate", method = RequestMethod.GET)
  public ValidationResult validate(CommandContext<GskDoumentExportDtoIn> ctx) {
    return validateAbl.validate(ctx.getUri().getAwid());
  }

}
