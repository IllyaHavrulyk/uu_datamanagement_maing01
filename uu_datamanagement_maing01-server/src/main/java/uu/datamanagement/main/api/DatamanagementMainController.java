package uu.datamanagement.main.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.DatamanagementMainAbl;
import uu.datamanagement.main.api.dto.ClearStorageDtoIn;
import uu.datamanagement.main.api.dto.ClearStorageDtoOut;
import uu.datamanagement.main.api.dto.DatamanagementMainInitDtoIn;
import uu.datamanagement.main.api.dto.DatamanagementMainInitDtoOut;

@CommandController
public final class DatamanagementMainController {

  @Inject
  private DatamanagementMainAbl datamanagementMainAbl;

  @Command(path = "init", method = POST)
  public DatamanagementMainInitDtoOut create(CommandContext<DatamanagementMainInitDtoIn> ctx) {
    DatamanagementMainInitDtoOut dtoOut = datamanagementMainAbl.init(ctx.getUri().getAwid(), ctx.getDtoIn());
    return dtoOut;
  }

}
