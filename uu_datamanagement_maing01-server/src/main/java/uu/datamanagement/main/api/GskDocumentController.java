package uu.datamanagement.main.api;

import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.app.server.annotation.DownloadCommand;
import uu.app.server.dto.DownloadableResourceDtoOut;
import uu.datamanagement.main.abl.GskDocumentAbl;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoIn;
import uu.datamanagement.main.api.dto.GskDocumentCreateDtoOut;
import uu.datamanagement.main.api.dto.GskDoumentExportDtoIn;

@CommandController
public class GskDocumentController {

  private static final Logger logger = LogManager.getLogger(GskDocumentController.class);

  private final GskDocumentAbl gskDocumentAbl;

  @Inject
  public GskDocumentController(GskDocumentAbl gskDocumentAbl) {
    this.gskDocumentAbl = gskDocumentAbl;
  }

  @Command(path = "gskdocument/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public GskDocumentCreateDtoOut create(CommandContext<GskDocumentCreateDtoIn> ctx) {
    logger.info("uuCMD gskdocument/create started with DTO in: {}", ctx.getDtoIn());
    GskDocumentCreateDtoOut dtoOut = gskDocumentAbl.create(ctx.getUri().getAwid(), ctx.getDtoIn());
    logger.info("uuCMD gskdocument/create completed with {}", dtoOut);
    return dtoOut;
  }

  @DownloadCommand(path = "/export", method = RequestMethod.GET)
  public DownloadableResourceDtoOut export(CommandContext<GskDoumentExportDtoIn> ctx) {
    logger.info("uuCMD /export started with DTO in: {}", ctx.getDtoIn());
    DownloadableResourceDtoOut dtoOut = gskDocumentAbl.export(ctx.getUri().getAwid(), ctx.getDtoIn());
    logger.info("uuCMD /export completed with {}", dtoOut);
    return dtoOut;
  }

}
