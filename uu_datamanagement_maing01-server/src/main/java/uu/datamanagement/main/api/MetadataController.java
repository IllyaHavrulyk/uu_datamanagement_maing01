package uu.datamanagement.main.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.RequestMethod;
import uu.app.server.CommandContext;
import uu.app.server.annotation.Command;
import uu.app.server.annotation.CommandController;
import uu.datamanagement.main.abl.MetadataAbl;
import uu.datamanagement.main.api.dto.MetadataListDtoIn;
import uu.datamanagement.main.api.dto.MetadataListDtoOut;

@CommandController
public class MetadataController {

  private final MetadataAbl metadataAbl;

  @Inject
  public MetadataController(MetadataAbl metadataAbl) {
    this.metadataAbl = metadataAbl;
  }

  @Command(path = "metadata/list", method = GET)
  public MetadataListDtoOut list(CommandContext<MetadataListDtoIn> ctx) {
    return metadataAbl.list(ctx.getUri().getAwid(), ctx.getDtoIn());
  }

}
