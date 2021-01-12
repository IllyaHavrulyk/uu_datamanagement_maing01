package uu.datamanagement.main.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.ClearStorageAbl;
import uu.datamanagement.main.api.dto.ClearStorageDtoIn;
import uu.datamanagement.main.api.dto.ClearStorageDtoOut;

@CommandController
public class ClearStorageController {

  private static final Logger logger = LogManager.getLogger(ClearStorageController.class);

  private final ClearStorageAbl clearStorageAbl;

  @Inject
  public ClearStorageController(ClearStorageAbl clearStorageAbl) {
    this.clearStorageAbl = clearStorageAbl;
  }

  @Command(path = "clean", method = POST)
  public ClearStorageDtoOut clean(CommandContext<ClearStorageDtoIn> ctx) {
    logger.info("uuCMD /clean started with DTO in: {}", ctx.getDtoIn());
    ClearStorageDtoOut dtoOut = clearStorageAbl.clean(ctx.getUri().getAwid(), ctx.getDtoIn());
    logger.info("uuCMD /clean completed with {}", dtoOut);
    return dtoOut;
  }

}
