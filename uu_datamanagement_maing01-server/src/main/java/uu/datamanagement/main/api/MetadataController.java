package uu.datamanagement.main.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.MetadataAbl;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoIn;
import uu.datamanagement.main.api.dto.MetadataUpdateDtoOut;

@CommandController
public class MetadataController {

  private static final Logger logger = LogManager.getLogger(MetadataController.class);

  private final MetadataAbl metadataAbl;

  @Inject
  public MetadataController(MetadataAbl metadataAbl) {
    this.metadataAbl = metadataAbl;
  }

  @Command(path = "metadata/list", method = GET)
  public MetadataListDtoOut list(CommandContext<MetadataListDtoIn> ctx) {
    logger.info("uuCMD metadata/list started with DTO in: {}", ctx.getDtoIn());
    MetadataListDtoOut dtoOut = metadataAbl.list(ctx.getUri().getAwid(), ctx.getDtoIn());
    logger.info("uuCMD metadata/list completed with {}", dtoOut);
    return dtoOut;
  }

  @Command(path = "metadata/update", method = POST)
  public MetadataUpdateDtoOut update(CommandContext<MetadataUpdateDtoIn> ctx) {
    logger.info("uuCMD iec61968100/create started with DTO in: {}", ctx.getDtoIn());
    MetadataUpdateDtoOut dtoOut = metadataAbl.update(ctx.getUri().getAwid(), ctx.getDtoIn());
    logger.info("uuCMD iec61968100/create completed with {}", dtoOut);
    return dtoOut;
  }

}
